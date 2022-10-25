package payex.no.tvapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShowRating {
    int id;
    String showName;
    double rating;
}
