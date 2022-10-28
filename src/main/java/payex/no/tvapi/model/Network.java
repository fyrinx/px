package payex.no.tvapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Network {
    int id,showCount;
    double averageRating,topRating;
    String topRatedShow,networkName;
}
