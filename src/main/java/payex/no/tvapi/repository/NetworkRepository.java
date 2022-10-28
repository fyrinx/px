package payex.no.tvapi.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import payex.no.tvapi.model.Network;
import payex.no.tvapi.model.NetworkFromApi;

@Repository
public class NetworkRepository implements NetworkDao {
    private static final String INSERT_NETWORK="INSERT INTO network(id,name,show_count,average_rating,top_rating,top_show_id) VALUES (?,?,?,?,?,?)";
    private static final String GET_TOP_NETWORK="select network.*, series_name from network join series on series.id=top_show_id order by top_rating desc";
    @Autowired
    private JdbcTemplate db;
    @Override
    public boolean saveNetwork(NetworkFromApi network) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Network getNetwork() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean saveNetworks(List<NetworkFromApi> networks) {
        final int batchSize=100;
        
        for(int j=0;j<batchSize;j+=batchSize) {
            final List<NetworkFromApi> batchList= networks.subList(j, j+batchSize > networks.size() ? networks.size() : j+batchSize);
            try {
                db.batchUpdate(INSERT_NETWORK, new BatchPreparedStatementSetter(){
                    @Override
                    public void setValues(PreparedStatement ps,int i) throws SQLException {
                        NetworkFromApi n = batchList.get(i);
                        ps.setInt(1, n.getId());
                        ps.setString(2, n.getName());
                        ps.setInt(3,n.getShowCount());
                        ps.setDouble(4, n.calculateAverage());
                        ps.setDouble(5, n.getTopRating());
                        ps.setInt(6, n.getTopRatedShow());
                    
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
    public List<Network> getTopSeriesPerNetwork(){
        return db.query(GET_TOP_NETWORK,new RowMapper<Network>(){  
             @Override
             public Network mapRow(ResultSet rs, int rownum) throws  SQLException    {  
                   Network s=new Network();  
                   s.setId(rs.getInt("id"));
                   s.setNetworkName(rs.getString("name"));
                   s.setShowCount(rs.getInt("show_count"));
                   s.setTopRatedShow(rs.getString("series_name")); 
                   s.setTopRating(rs.getDouble("top_rating"));  
                   s.setAverageRating(rs.getDouble("average_rating"));

                   return s;  
                 }
             }); 
     }
    
}
