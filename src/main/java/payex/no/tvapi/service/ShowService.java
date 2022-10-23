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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;  
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import payex.no.tvapi.model.Show;
import payex.no.tvapi.model.ShowFromApi;
import payex.no.tvapi.model.ShowGenre;
import payex.no.tvapi.repository.ShowRepository;

@Service
public class ShowService {
    @Autowired
    ShowRepository showRepository;
    
    JSONObject o;
    ObjectMapper mapper;
    public JsonNode queryShowApi(String showName) throws JsonMappingException, JsonProcessingException{
        String url="https://api.tvmaze.com/search/shows?q=";
        RestTemplate rt=new RestTemplate();
        ObjectMapper mapper=new ObjectMapper();
        try {
            Object[] data = rt.getForObject(url+showName,Object[].class);
            Map<String,Object> om=mapper.convertValue(data[0], Map.class);
            JsonNode sApi=mapper.convertValue(om.get("show"),JsonNode.class);
            System.out.println(sApi.get("rating").get("average").asDouble());
            return data[0];
        } 
        catch(HttpClientErrorException.TooManyRequests e) {
            e.printStackTrace();
            
            return e; 
        }
        catch(NullPointerException e){
            
            return e;
        }
    }
    public JsonNode[] queryEpisodes(int showId){
        String url="https://api.tvmaze.com/shows/";
        RestTemplate rt=new RestTemplate();
        ObjectMapper mapper=new ObjectMapper();
        JsonNode[] os=new JsonNode[1];
        try {
            JsonNode[] data = rt.getForObject(url+showId+"/episodes",JsonNode[].class);
            
            System.out.println(data[0]);
            return data;
        } 
        catch(HttpClientErrorException.TooManyRequests e) {
            e.printStackTrace();
            os[0]=mapper.convertValue(e, JsonNode.class);
            return os; 
        }
        catch(NullPointerException e){
            os[0]=mapper.convertValue(e, JsonNode.class);
            return os;
        }
    }
    @Transactional
    public String batchInsert(String[] showNames){
        int genreId=0;
        // hasmap with genre as a key, generated id and the count in array 
        Map<String,Integer[]> genres= new HashMap<String,Integer[]>();
        List<JsonNode> episodes=new ArrayList<JsonNode>();
        HashSet<JsonNode> networks=new HashSet<JsonNode>();
        List<JsonNode> shows=new ArrayList<JsonNode>();
        // showId and genreId
        Map<Integer,Integer> showGenres=new HashMap<Integer,Integer>();
        JsonNode node;
        for(int i=0;i<showNames.length;i++){
            try {
                node=queryShowApi(showNames[i]);
                shows.add(node);
                episodes.addAll(Arrays.asList(queryEpisodes(node.get(shows.size()-1).get("id").asInt())));
                networks.add(node.get("network"));
                String genre;
                JsonNode fG= node.get("genres");
                if(fG.isArray()){
                    for (JsonNode ob : fG){
                        genre=ob.asText();
                        if(genres.containsKey(genre)){
                            genres.put(genre,new Integer[] {genres.get(genre)[0],genres.get(genre)[1]++});
                            
                        } else {
                            genreId++;
                            genres.put(ob.asText(),new Integer[]{genreId,1});
                            showGenres.put(node.get("id").asInt(),genreId);
                        }
                        
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
        
    }

    
}
