package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MoviesResponse;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class
		List<Movie> result = new ArrayList<Movie>();
		MoviesResponse data = movieDataService.fetchAll();
		int id = 1;
		for(MovieData movie: data) {
			if(movie.getTitle().length() == queryText.length()) continue;
			if(movie.getTitle().toLowerCase().matches(".*\\b" + queryText.toLowerCase() + "\\b.*")) {
				Movie m = new Movie(movie.getTitle());
				m.setActors(movie.getCast());
				m.setId((long) id++);
				result.add(m);
			}
		}
		return result;
	}
}
