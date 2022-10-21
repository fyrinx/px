package payex.no.repository;

import java.util.List;

import payex.no.tvapi.model.Show;

public interface ShowDao {
    void saveShow(Show show);
    Show getById(String id);
    String deleteById();
    List<Show> allShows();
}
