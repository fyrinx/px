package payex.no.tvapi.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import payex.no.tvapi.model.Network;
import payex.no.tvapi.model.NetworkFromApi;

@Repository
public class NetworkRepository implements NetworkDao {
    private static final String INSERT_NETWORK="INSERT INTO network(id,name) VALUES (?,?)";
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
    
}
