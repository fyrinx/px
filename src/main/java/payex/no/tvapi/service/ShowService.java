package payex.no.tvapi.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import payex.no.tvapi.model.Show;
import payex.no.tvapi.repository.ShowRepository;

@Service
public class ShowService {
    @Autowired
    ShowRepository showRepository;

    public Object queryShowApi(String showName){
        String url="https://api.tvmaze.com/search/shows?q=";
        RestTemplate rt=new RestTemplate();
        
        try {
            Object[] data = rt.getForObject(url+showName,Object[].class);
            return data[0];
        } 
        catch(HttpClientErrorException.TooManyRequests e) {
            return e;
            
        }
    }
}
