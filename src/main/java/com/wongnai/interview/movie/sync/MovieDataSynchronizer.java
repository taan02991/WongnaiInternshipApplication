package com.wongnai.interview.movie.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MovieDataService;
import com.wongnai.interview.movie.external.MoviesResponse;

@Component
public class MovieDataSynchronizer {
	@Autowired
	private MovieDataService movieDataService;

	@Autowired
	private MovieRepository movieRepository;
	
	public static HashMap<String, HashSet<Long>> invertedIndex = new HashMap<String, HashSet<Long>>();

	@Transactional
	public void forceSync() {
		//TODO: implement this to sync movie into repository
		if(movieRepository.count() > 0) return;
		List<Movie> result =  new ArrayList<Movie>();
		MoviesResponse response = movieDataService.fetchAll();
		for(MovieData data: response) {
			Movie m = new Movie(data.getTitle());
			m.setActors(data.getCast());
			result.add(m);
		}
		movieRepository.saveAll(result);
		
		for(Movie m: movieRepository.findAll()) {
			long id = m.getId();
			String[] name = m.getName().split(" ");
			for(String word: name) {
				word = word.toLowerCase();
				if(invertedIndex.containsKey(word)) {
					invertedIndex.get(word).add(id);
				}
				else {
					HashSet<Long> hs = new HashSet<Long>();
					hs.add(id);
					invertedIndex.put(word, hs);					
				}
			}
		}
	}
}
