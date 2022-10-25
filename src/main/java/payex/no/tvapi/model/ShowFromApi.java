package payex.no.tvapi.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowFromApi {
    int id,episodeCount,releasedEpisodeCount;
    double rating;
    NetworkFromApi network;
    String[] genres,days;
    String name,summary,ended;
    @JsonProperty("rating")
    private void unpackNameFromNestedObject(Map<String, Double> rt) {
        if(rt.get("average")==null){
            rating=0.0;
        }else {
            rating = rt.get("average");
        }
    }
    @JsonProperty("schedule")
    private void unpackFromNestedObject(ShowTime d){
        days=d.getDays();
    }

}

@Data
@NoArgsConstructor
class ShowTime {
    String time;
    String[] days;
}
