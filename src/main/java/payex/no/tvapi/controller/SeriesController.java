package payex.no.tvapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeriesController {
    @GetMapping("/")
    public String hello(){
        return "Så kjekt det var å se deg! :D";
    }
}
