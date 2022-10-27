package payex.no.tvapi.repository;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;

import payex.no.tvapi.model.Episode;
import payex.no.tvapi.model.Genre;
import payex.no.tvapi.model.Show;
import payex.no.tvapi.model.ShowDays;
import payex.no.tvapi.model.ShowFromApi;
import payex.no.tvapi.model.ShowGenre;
import payex.no.tvapi.model.ShowRating;

@Repository
public class ShowRepository implements ShowDao {

    private static final String INSERT_SHOW="INSERT INTO series(id,series_name,network_id,rating,episode_count,released_episode_count,summary,ended,days) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String GET_SHOW_LIST="SELECT series.*, network.name as network_name, FROM series left join network on network.id=network_id";
    private static final String INSERT_EPISODE="INSERT INTO episode(id,series_id,network_id,season_number,episode_number,episode_name,rating) VALUES(?,?,?,?,?,?,?)";
    
    @Autowired
    private JdbcTemplate db;

    
    @Override
    public boolean saveShow(ShowFromApi show) {
        db.update(INSERT_SHOW, show.getId(),show.getName(),
        show.getNetwork().getId(),show.getRating(),show.getEpisodeCount(),show.getReleasedEpisodeCount());
        return false;
    }

    public List<ShowRating> getTopTen(){
        return db.query("SELECT id,series_name,rating from series order by rating desc limit 10",new RowMapper<ShowRating>(){  
            @Override  
            public ShowRating mapRow(ResultSet rs, int rownumber) throws  SQLException    {  
                  ShowRating s=new ShowRating();  
                  s.setId(rs.getInt("id"));  
                  s.setShowName(rs.getString("series_name")); 
                  s.setRating(rs.getDouble("rating"));  
                  return s;  
                }  
            }); 
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
                        ps.setString(7,show.getSummary());
                        if(show.getEnded()!=""){
                            StringBuilder sb=new StringBuilder();
                            String [] s =show.getDays();
                            for(String d : s){
                                sb.append(d+";");
                            }
                            
                            ps.setInt(8,0);
                            ps.setString(9,sb.toString());
                        } else {
                            ps.setInt(8,1);
                            ps.setString(9,"");
                        }
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
    public boolean saveGenres(List<Genre> genres) { 
            try {
                db.batchUpdate("INSERT into genre(id,genre) values(?,?)", new BatchPreparedStatementSetter(){
                    @Override
                    public void setValues(PreparedStatement ps,int i) throws SQLException {
                        Genre e = genres.get(i);
                        ps.setInt(1, e.getId());
                        ps.setString(2,e.getGenreName());
                    
                    }
                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        return true;
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
        return db.query(GET_SHOW_LIST,new RowMapper<Show>(){  
            @Override  
            public Show mapRow(ResultSet rs, int rownumber) throws  SQLException    {  
                  Show s=new Show();  
                  s.setId(rs.getInt("id"));  
                  s.setShowName(rs.getString("series_name")); 
                  s.setRating(rs.getDouble("rating"));  
                  s.setSummary(rs.getString("summary"));
                  s.setNetworkId(rs.getInt("network_id"));
                  s.setEpisodeCount(rs.getInt("episode_count")); 
                  s.setReleasedEpisodeCount(rs.getInt("released_episode_count"));
                  return s;  
                }  
            });  
    }
    @Override
    public List<Genre> allGenresForShow() {
        // TODO Auto-generated method stub
        return db.query("SELECT series_genre.*, genre from series_genre join genre on genre.id=genre_id",new RowMapper<Genre>(){  
            @Override  
            public Genre mapRow(ResultSet rs, int rownumber) throws  SQLException    {  
                  Genre s=new Genre();  
                  s.setId(rs.getInt("id"));  
                  s.setGenreName(rs.getString("genre")); 
                  return s;  
                }  
            });  
    }
    @Override
    public boolean saveShowGenres(List<ShowGenre> genres) {
        final int batchSize=200;
        for(int j=0;j<genres.size();j+=batchSize) {
            List<ShowGenre> batchList= genres.subList(j, j+batchSize > genres.size() ? genres.size() : j+batchSize);
            try {
                db.batchUpdate("INSERT INTO series_genre(series_id, genre_id) VALUES (?, ?)", new BatchPreparedStatementSetter(){
                    @Override
                    public void setValues(PreparedStatement ps,int i) throws SQLException {
                        ShowGenre e = batchList.get(i);
                        ps.setInt(1, e.getShowId());
                        ps.setInt(2,e.getGenreId());
                    
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
    public boolean setSchedules(List<ShowDays> sds){
        final int batchSize=200;
        for(int j=0;j<sds.size();j+=batchSize) {
            List<ShowDays> batchList= sds.subList(j, j+batchSize > sds.size() ? sds.size() : j+batchSize);
            try {
                db.batchUpdate("INSERT INTO next_week(series_id, days) VALUES (?,?)", 
                new BatchPreparedStatementSetter(){
                    @Override
                    public void setValues(PreparedStatement ps,int i) throws SQLException {
                        
                        ShowDays e = batchList.get(i);
                        ps.setInt(1, e.getId());
                        String eDays[]=e.getDays();
                        StringBuilder sb=new StringBuilder();
                        String[] days={"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday"};
                        for(int k=0;k<days.length;k++){
                            for(int j=0;j<eDays.length;j++){
                                if(eDays[j].equals(days[k])){
                                    sb.append(getEpisode(e.getLastSeason(),(e.getLastReleasedEpisode()+j)+1));
                                    
                                }
                                
                            }
                            sb.append(";");
                            
                        }
                        sb.replace(sb.length()-1,sb.length(),"");
                        ps.setString(2, sb.toString());
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
    
    private String getEpisode(int s,int e){
        return String.format("%02d%02d",s,e);
    }
}
