package payex.no.tvapi.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import payex.no.tvapi.model.Genre;
import payex.no.tvapi.model.NetworkFromApi;
import payex.no.tvapi.model.Show;
import payex.no.tvapi.model.ShowDays;
import payex.no.tvapi.model.ShowFromApi;
import payex.no.tvapi.model.ShowGenre;
import payex.no.tvapi.model.ShowRating;
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
    public ShowFromApi queryShowApi(String showName) throws JsonMappingException, JsonProcessingException,InterruptedException{
        String url="https://api.tvmaze.com/search/shows?q=";
        RestTemplate rt=new RestTemplate();
        ObjectMapper mapper=new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        try {
            Object[] data = rt.getForObject(url+showName,Object[].class);
            Map<String,Object> om=mapper.convertValue(data[0], Map.class);
            ShowFromApi sApi=mapper.convertValue(om.get("show"),ShowFromApi.class);
            //System.out.println(sApi.get("rating").get("average").asDouble());
            return sApi;
        } 
        catch(HttpClientErrorException.TooManyRequests e) {
            System.out.println("Handling 429...");
            Thread.sleep(5000);
            
            return queryShowApi(showName); 
        }
        catch(NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }
    public Episode[] queryEpisodes(int showId) throws InterruptedException{
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
              
            System.out.println("Handling 429...");
            Thread.sleep(5000);
            
            return queryEpisodes(showId); 

            //os[0]=mapper.convertValue(e, JsonNode.class);
        }
        catch(NullPointerException e){
            //os[0]=mapper.convertValue(e, JsonNode.class);
            e.printStackTrace();
            return null;
        }
    }
    
    @Transactional
    public String batchInsert(String[] showNames) throws InterruptedException{
        int genreId=0;
        // hasmap with genre as a key, generated id and the count in array 
        Map<String,Integer[]> genres= new HashMap<String,Integer[]>();
        List<Episode> episodes=new ArrayList<Episode>();
        List<Episode> currentEpisodes;
        HashMap<Integer,NetworkFromApi> networks=new HashMap<Integer,NetworkFromApi>();
        List<ShowFromApi> shows=new ArrayList<ShowFromApi>();
        // showId and genreId
        List<ShowGenre> showGenres=new ArrayList<ShowGenre>();
        int episodeCount=0;
        List<ShowDays> showDays=new ArrayList<ShowDays>();
        for(int i=0;i<showNames.length;i++){
            try {
                final ShowFromApi node=queryShowApi(showNames[i]);
                shows.add(node);
                currentEpisodes=Arrays.asList(queryEpisodes(node.getId()));
                episodes.addAll(currentEpisodes);
                episodeCount=currentEpisodes.size();
                int releasedEpisodeCount=getLatestEpisode(currentEpisodes);
                if(node.getNetwork()!=null){

                    networks.put(node.getNetwork().getId(),node.getNetwork());
                    episodes.forEach((e)->{
                        e.setShowName(node.getName());
                        e.setShowId(node.getId());
                        e.setNetworkId(node.getNetwork().getId());
                    });
                }
                if(node.getEnded()==null){
                    int season=currentEpisodes.get(currentEpisodes.size()-1).getSeason();
                    
            
                    showDays.add(new ShowDays(node.getId(),season,releasedEpisodeCount,node.getDays()));
                }
                node.setEpisodeCount(episodeCount);
                node.setReleasedEpisodeCount(releasedEpisodeCount);
                String genre;
                String[] fG= node.getGenres();
                
                for (int j=0;j<fG.length;j++){
                    genre=fG[j];
                    if(genres.containsKey(genre)){
                        genres.put(genre,new Integer[] {genres.get(genre)[0],genres.get(genre)[1]++});
                        showGenres.add(new ShowGenre(shows.get(i).getId(),genres.get(genre)[0]));
                    } else {
                        genreId++;
                        genres.put(genre,new Integer[]{genreId,1});
                        showGenres.add(new ShowGenre(shows.get(i).getId(),genreId));
                    }
                    
                }
            } catch (InterruptedException e){
              System.out.println("Sleep interrupted");
              return e.toString();
            }
              catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                return e.toString();
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                return e.toString();
            }


        }
        List<NetworkFromApi> ns=new ArrayList<NetworkFromApi>(networks.values());
        networkRepository.saveNetworks(ns);
        
        List<Genre> genreList=new ArrayList<Genre>();
        for(Map.Entry<String,Integer[]> entry : genres.entrySet()){
            genreList.add(new Genre(entry.getValue()[0], entry.getValue()[1], entry.getKey()));
        }
        showRepository.saveGenres(genreList);
        System.out.println("Saved genres");
        showRepository.saveShows(shows);
        System.out.println("Saved shows");
        showRepository.saveShowGenres(showGenres);
        System.out.println("Saved the genres of shows");
        showRepository.saveEpisodes(episodes);
        System.out.println("Saved episodes");
        showRepository.setSchedules(showDays);
        System.out.println("Saved episode days");
        return "Ok";
    }
    
    public List<Show> getAllShows(){
        return showRepository.allShows();
    }
    public List<ShowRating> getTopTen(){
        return showRepository.getTopTen();
    }

    public int getLatestEpisode(List<Episode> es){
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dtEpisode;
        LocalDate now=LocalDate.now();
        int minI=0;
        for(int i=es.size()-1;i>0;i--){
            dtEpisode=LocalDate.parse(es.get(i).getAirdate(),dtf);
            if(dtEpisode.compareTo(now)<=0){
                minI=i;
                break;
            }
        }
        return minI;
    }
}
