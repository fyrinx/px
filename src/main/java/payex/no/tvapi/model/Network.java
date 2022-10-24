package payex.no.tvapi.model;

import lombok.Data;

@Data
public class Network {
    int rank,showcount;
    double averageRating,topRating;
    String topRatedShow;
}
