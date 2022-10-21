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
	"https://api.tvmaze.com/search/shows?q=tullball",
"https://api.tvmaze.com/singlesearch/shows?q=girls&embed=episodes",
"https://api.tvmaze.com/lookup/shows?imdb=tt0944947",
"https://api.tvmaze.com/search/shows?q=kekwist",
"https://api.tvmaze.com/schedule/web?date=2020-05-29&country=US",
"https://api.tvmaze.com/search/people?q=:query",
"https://api.tvmaze.com/shows/1?embed=nextepisode",
"https://api.tvmaze.com/shows/1/episodebynumber?season=1&number=1",
"https://api.tvmaze.com/shows/1?embed[]=episodes&embed[]=cast"};
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
		for(int i=0;i<100;i++){
			url=new URL(links[i%10]);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			if(con.getResponseCode()==429){
				trigger=true;
			}
			con.disconnect();

			
		}
		
		assertEquals(true,trigger);
		
	}

}
