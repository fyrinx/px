package payex.no.tvapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="api/v1/")
public class SeriesController {
    @GetMapping("/")
    public String hello(){
        return "Så kjekt det var å se deg! :D";
    }
}
