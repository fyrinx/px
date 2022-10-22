package payex.no.tvapi.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import payex.no.tvapi.model.Show;

@Repository
public class ShowRepository implements ShowDao {

    private static final String INSERT_SHOW="INSERT INTO series(id,series_name,networkd_id,rating,episode_count,released_episode_count)";
    
    @Autowired
    private JdbcTemplate db;
    @Override
    public void saveShow(Show show) {
        db.update(INSERT_SHOW, show.getShowId(),show.getShowName(),
        show.getNetworkId(),show.getRating(),show.getEpisodeCount(),show.getReleasedEpisodeCount());
        
    }

    @Override
    public Show getById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String deleteById() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Show> allShows() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
