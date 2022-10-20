package payex.no.tvapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TvapiApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	void connectTvMaze() throws IOException{
		URL url = new URL("https://www.tvmaze.com/api");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.connect();
		assertEquals(200, con.getResponseCode());
	}

}
