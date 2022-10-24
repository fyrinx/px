package payex.no.tvapi.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

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
    String[] genres;
    String name,summary;
    @JsonProperty("rating")
    private void unpackNameFromNestedObject(Map<String, Double> rt) {
    rating = rt.get("average");
}
}
