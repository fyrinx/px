package payex.no.tvapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TvapiApplicationTests {
	String[] links={"https://api.tvmaze.com/search/shows?q=girls",
	"https://api.tvmaze.com/lookup/shows?thetvdb=81189",
"https://api.tvmaze.com/singlesearch/shows?q=girls&embed=episodes",
"https://api.tvmaze.com/lookup/shows?imdb=tt0944947",
"https://api.tvmaze.com/schedule?country=US&date=2014-12-01",
"https://api.tvmaze.com/schedule/web?date=2020-05-29&country=US",
"https://api.tvmaze.com/shows/1?embed=cast",
"https://api.tvmaze.com/alternatelists/1?embed=alternateepisodes",
"https://api.tvmaze.com/shows/1/episodebynumber?season=1&number=1",
"https://api.tvmaze.com/shows/4/seasons"};
	@Test
	void contextLoads() {
	}
	@Test
	void connectTvMaze() throws IOException{
		URL url = new URL("https://api.tvmaze.com/shows");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.connect();
		assertEquals(200, con.getResponseCode());
	}
	//@Test
	void connectExess() throws IOException{
		URL url;
		HttpURLConnection con;
		Boolean trigger=false;
		for(int i=0;i<400;i++){
			url=new URL(links[i%10]);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			if(con.getResponseCode()==429){
				trigger=true;
			}
			assertEquals(true,trigger);
		}
		
	}

}
