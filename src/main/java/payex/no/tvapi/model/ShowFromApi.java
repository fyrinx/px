package payex.no.tvapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;


public class ShowFromApi {
    int id;
    Rating rating;
    NetworkApi network;
    String[] genres;
    String name,summary;
}

class Rating {
    double average;
}

class NetworkApi {
    int id;
    String name;

}