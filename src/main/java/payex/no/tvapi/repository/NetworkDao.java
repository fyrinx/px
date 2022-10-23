package payex.no.tvapi.repository;

import payex.no.tvapi.model.Network;

public interface NetworkDao {
    String saveNetwork(Network network);
    Network getNetwork();
}
