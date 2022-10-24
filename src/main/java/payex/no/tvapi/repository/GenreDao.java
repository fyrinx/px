package payex.no.tvapi.repository;

import com.fasterxml.jackson.databind.JsonNode;

public interface GenreDao {
    String saveGenre(JsonNode genre);
}
