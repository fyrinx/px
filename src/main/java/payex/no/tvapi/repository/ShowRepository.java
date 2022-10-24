package payex.no.tvapi.repository;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;

import payex.no.tvapi.model.Episode;
import payex.no.tvapi.model.Genre;
import payex.no.tvapi.model.Show;
import payex.no.tvapi.model.ShowFromApi;

@Repository
public class ShowRepository implements ShowDao {

    private static final String INSERT_SHOW="INSERT INTO series(id,series_name,network_id,rating,episode_count,released_episode_count) VALUES (?,?,?,?,?,?)";
    private static final String INSERT_EPISODE="INSERT INTO episode(id,series_id,network_id,season_number,episode_number,episode_name,rating) VALUES(?,?,?,?,?,?,?)";
    @Autowired
    private JdbcTemplate db;

    
    @Override
    public boolean saveShow(ShowFromApi show) {
        db.update(INSERT_SHOW, show.getId(),show.getName(),
        show.getNetwork().getId(),show.getRating(),show.getEpisodeCount(),show.getReleasedEpisodeCount());
        return false;
    }
    @Override
    public boolean saveShows(List<ShowFromApi> shows) {
        final int batchSize=60;
        
        for(int j=0;j<shows.size();j+=batchSize) {
            List<ShowFromApi> batchList= shows.subList(j, j+batchSize > shows.size() ? shows.size() : j+batchSize);
            try {
                db.batchUpdate(INSERT_SHOW, new BatchPreparedStatementSetter(){
                    @Override
                    public void setValues(PreparedStatement ps,int i) throws SQLException {
                        ShowFromApi show = batchList.get(i);
                        ps.setInt(1, show.getId());
                        ps.setString(2, show.getName());
                        if(show.getNetwork()==null){
                            ps.setInt(3,0);   
                        }else {
                            ps.setInt(3,show.getNetwork().getId());
                        }
                        
                        ps.setDouble(4,show.getRating());
                        ps.setInt(5,show.getEpisodeCount());
                        ps.setInt(6,show.getReleasedEpisodeCount());
                    }
                    @Override
                    public int getBatchSize() {
                        return batchList.size();
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            
        }
        return true;
    }
    @Override
    public boolean saveEpisodes(List<Episode> episodes) {
        final int batchSize=100;
        
        for(int j=0;j<episodes.size();j+=batchSize) {
            List<Episode> batchList= episodes.subList(j, j+batchSize > episodes.size() ? episodes.size() : j+batchSize);
            try {
                db.batchUpdate(INSERT_EPISODE, new BatchPreparedStatementSetter(){
                    @Override
                    public void setValues(PreparedStatement ps,int i) throws SQLException {
                        Episode e = batchList.get(i);
                        
                        ps.setInt(1, e.getId());
                        ps.setInt(2, e.getShowId());
                        ps.setInt(3,e.getNetworkId());
                        ps.setDouble(4,e.getSeason());
                        ps.setInt(5,e.getNumber());
                        ps.setString(6,e.getName());
                        ps.setDouble(7,e.getRating());
                    
                    }
                    @Override
                    public int getBatchSize() {
                        return batchList.size();
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            
        }
        return true;
        
    }
    @Override
    public boolean saveGenres(Map<String, Integer[]> genres) {
        
        return false;
    }
    @Override
    public Show getById(int id) {
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
    @Override
    public List<Genre> allGenres() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
