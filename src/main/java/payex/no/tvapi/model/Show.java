package payex.no.tvapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {
    double rating;
    String showName,summary;
    int networkId,showId,episodeCount,releasedEpisodeCount;
    String [] genres;
    
}
    
