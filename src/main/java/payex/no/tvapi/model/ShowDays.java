package payex.no.tvapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShowDays {
    int id,lastSeason,lastEpisode;
    String[] days;

}
