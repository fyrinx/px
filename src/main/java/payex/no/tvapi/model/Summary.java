package payex.no.tvapi.model;

import java.util.ArrayList;

public class Summary {
    ArrayList<String> genres;
    String showName,network;
    int showId,episodeCount,releasedEpisodeCount;
    public Summary(int showId,String showName,String network, int episodeCount, int releasedEpisodeCount) {
        this.showId = showId;
        this.showName = showName;
        this.network = network;
        this.episodeCount = episodeCount;
        this.releasedEpisodeCount = releasedEpisodeCount;
        genres=new ArrayList<String>();
    }
    public String getNetwork() {
        return network;
    }
    public void setNetwork(String network) {
        this.network = network;
    }
    public ArrayList<String> getGenres() {
        return genres;
    }
    public void addGenre(String genre) {
        genres.add(genre);
    }
    public int getEpisodeCount() {
        return episodeCount;
    }
    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }
    public int getReleasedEpisodeCount() {
        return releasedEpisodeCount;
    }
    public void setReleasedEpisodeCount(int releasedEpisodeCount) {
        this.releasedEpisodeCount = releasedEpisodeCount;
    }
}
