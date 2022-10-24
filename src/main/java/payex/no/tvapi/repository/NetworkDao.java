package payex.no.tvapi.repository;

import java.util.List;

import payex.no.tvapi.model.Network;
import payex.no.tvapi.model.NetworkFromApi;

public interface NetworkDao {
    boolean saveNetwork(NetworkFromApi network);
    boolean saveNetworks(List<NetworkFromApi> networks);
    Network getNetwork();
}
