package payex.no.tvapi.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Episode {
    
    int id,showId,number,season,networkId;

    double rating;
    String name,airdate,networkName;
    String showName;
    
    @JsonProperty("rating")
    private void unpackNameFromNestedObject(Map<String, Double> rt) {
    if(rt.get("average")==null){
        rating=0.0;
    }else {
        rating = rt.get("average");
    }
    }
}
