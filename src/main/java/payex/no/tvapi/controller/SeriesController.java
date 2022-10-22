package payex.no.tvapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
    public String addShows(@RequestBody String[] data){
        if(data.length==60){
            return "Ok";
        }else {
            return "Error";
        }
    }
    @GetMapping("/testapi")
    public Object testapi(){
        Object show=showService.queryShowApi("girls");
        System.out.println(show);
        return show;

    }
}
