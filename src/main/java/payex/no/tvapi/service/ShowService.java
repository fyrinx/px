package payex.no.tvapi.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.json.JSONObject;  
import org.springframework.boot.json.JsonParser;

import payex.no.tvapi.model.Episode;
import payex.no.tvapi.model.NetworkFromApi;
import payex.no.tvapi.model.Show;
import payex.no.tvapi.model.ShowFromApi;
import payex.no.tvapi.model.ShowGenre;
import payex.no.tvapi.repository.NetworkRepository;
import payex.no.tvapi.repository.ShowRepository;

@Service
public class ShowService {
    @Autowired
    NetworkRepository networkRepository;
    @Autowired
    ShowRepository showRepository;
    
    JSONObject o;
    ObjectMapper mapper;
    public ShowFromApi queryShowApi(String showName) throws JsonMappingException, JsonProcessingException{
        String url="https://api.tvmaze.com/search/shows?q=";
        RestTemplate rt=new RestTemplate();
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            Object[] data = rt.getForObject(url+showName,Object[].class);
            Map<String,Object> om=mapper.convertValue(data[0], Map.class);
            ShowFromApi sApi=mapper.convertValue(om.get("show"),ShowFromApi.class);
            //System.out.println(sApi.get("rating").get("average").asDouble());
            return sApi;
        } 
        catch(HttpClientErrorException.TooManyRequests e) {
            e.printStackTrace();
            
            return null; 
        }
        catch(NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }
    public Episode[] queryEpisodes(int showId){
        String url="https://api.tvmaze.com/shows/";
        RestTemplate rt=new RestTemplate();
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            Object[] data = rt.getForObject(url+showId+"/episodes",Object[].class);
            Episode[] e=mapper.convertValue(data,Episode[].class);
            return e;
        } 
        catch(HttpClientErrorException.TooManyRequests e) {
            e.printStackTrace();
            //os[0]=mapper.convertValue(e, JsonNode.class);
            return null; 
        }
        catch(NullPointerException e){
            //os[0]=mapper.convertValue(e, JsonNode.class);
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public String batchInsert(String[] showNames){
        int genreId=0;
        // hasmap with genre as a key, generated id and the count in array 
        Map<String,Integer[]> genres= new HashMap<String,Integer[]>();
        List<Episode> episodes=new ArrayList<Episode>();
        HashMap<Integer,NetworkFromApi> networks=new HashMap<Integer,NetworkFromApi>();
        List<ShowFromApi> shows=new ArrayList<ShowFromApi>();
        // showId and genreId
        Map<Integer,Integer> showGenres=new HashMap<Integer,Integer>();
        
        for(int i=0;i<showNames.length;i++){
            try {
                final ShowFromApi node=queryShowApi(showNames[i]);
                shows.add(node);
                episodes.addAll(Arrays.asList(queryEpisodes(node.getId())));
                if(node.getNetwork()!=null){

                    networks.put(node.getNetwork().getId(),node.getNetwork());
                    episodes.forEach((e)->{e.setShowName(node.getName());e.setShowId(node.getId());e.setNetworkId(node.getNetwork().getId());});
                }
                
                String genre;
                String[] fG= node.getGenres();
                
                for (int j=0;j<fG.length;j++){
                    genre=fG[j];
                    if(genres.containsKey(genre)){
                        genres.put(genre,new Integer[] {genres.get(genre)[0],genres.get(genre)[1]++});
                        
                    } else {
                        genreId++;
                        genres.put(genre,new Integer[]{genreId,1});
                        showGenres.put(shows.get(i).getId(),genreId);
                    }
                    
                }
                
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                return e.toString();
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                return e.toString();
            }


        }
        List<NetworkFromApi> ns=new ArrayList<NetworkFromApi>(networks.values());
        networkRepository.saveNetworks(ns);
        showRepository.saveShows(shows);
        showRepository.saveEpisodes(episodes);
        return "Ok";
    }

    
}
