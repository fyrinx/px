package payex.no.tvapi.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import payex.no.tvapi.model.Episode;
import payex.no.tvapi.model.Show;
import payex.no.tvapi.model.ShowFromApi;
import payex.no.tvapi.model.ShowRating;
import payex.no.tvapi.service.ShowService;

@RestController
@RequestMapping(path="/api/v1")
public class SeriesController {

    String[] shows;

    @Autowired
    ShowService showService;
    @GetMapping("/hi")
    public String hello(){
        return "Så kjekt det var å se deg! :D";
    }
    @PostMapping("/insert")
    public String addShows(@RequestBody String[] data) throws InterruptedException{
        System.out.println("batch triggered");
        return showService.batchInsert(data);
    }
    @GetMapping("/getAllShow")
    public List<Show> getAllShows() {
        return showService.getAllShows();    
    }
    @GetMapping("/getTopTen")
    public List<ShowRating> getTopTen(){
        return showService.getTopTen();
    }
    
    
    @GetMapping("/testapi")
    public ShowFromApi testapi() throws JsonMappingException, JsonProcessingException,InterruptedException{
        ShowFromApi o=showService.queryShowApi("girls");
        
        //System.out.println(o);
        return o;

    }
    @GetMapping("/testEpisode")
    public Episode[] testEpisode() throws JsonMappingException, JsonProcessingException,InterruptedException{
        Episode[] os=showService.queryEpisodes(1);
        
        //System.out.println(o);
        return os;

    }

}
