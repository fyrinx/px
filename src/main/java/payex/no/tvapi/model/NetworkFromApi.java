package payex.no.tvapi.model;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetworkFromApi {
    int id,showCount,topRatedShow;
    String name;
    ArrayList<Double> ratings;
    public double calculateAverage(){
        double total=0;
        for(int i = 0; i < ratings.size(); i++){
            total+=ratings.get(i);
        }
        return total/ratings.size();
    }
    double topRating;

}
