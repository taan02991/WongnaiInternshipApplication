package com.wongnai.interview.movie.external;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class MovieDataServiceImpl implements MovieDataService {
	public static final String MOVIE_DATA_URL
			= "https://raw.githubusercontent.com/prust/wikipedia-movie-data/master/movies.json";

	@Autowired
	private RestOperations restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public MoviesResponse fetchAll() {
		//TODO:
		// Step 1 => Implement this method to download data from MOVIE_DATA_URL and fix any error you may found.
		// Please noted that you must only read data remotely and only from given source,
		// do not download and use local file or put the file anywhere else.
		MoviesResponse result = new MoviesResponse();
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(MOVIE_DATA_URL))
	                .build();

        HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String formatted = "{movies : " + response.body() + "}"; 
			JSONObject obj = new JSONObject(formatted);
			JSONArray arr = obj.getJSONArray("movies");
			for(int i = 0; i < arr.length(); i++) {
				JSONObject movieData = arr.getJSONObject(i);
				MovieData movie = new MovieData();
				movie.setTitle(movieData.getString("title"));
				movie.setYear(Integer.parseInt(movieData.getString("year")));
				movie.setGenres(this.formatArray(movieData.getString("genres")));
				movie.setCast(this.formatArray(movieData.getString("cast")));
				result.add(movie);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private List<String> formatArray(String s){
		List<String> result = new ArrayList<String>();
		if(s.length() > 2) {
			String str[] = s.substring(2, s.length() - 2).split("\",\"");
			result = Arrays.asList(str);			
		}
		return result;
	}
}
