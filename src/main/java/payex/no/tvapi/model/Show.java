package payex.no.tvapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {
    double rating;
    String showName,summary;
    int networkId,id,episodeCount,releasedEpisodeCount;
    @JsonProperty("genres")
    String [] genres;
    
}
    
