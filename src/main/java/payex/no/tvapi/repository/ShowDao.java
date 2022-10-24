package payex.no.tvapi.repository;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import payex.no.tvapi.model.Episode;
import payex.no.tvapi.model.Genre;
import payex.no.tvapi.model.Show;
import payex.no.tvapi.model.ShowFromApi;

public interface ShowDao {
    boolean saveShow(ShowFromApi show);
    boolean saveShows(List<ShowFromApi> show);
    boolean saveGenres(Map<String, Integer[]> genres);
    boolean saveEpisodes(List<Episode> episodes);
    Show getById(int id);
    String deleteById();
    List<Show> allShows();
    List<Genre> allGenres();
    
}
