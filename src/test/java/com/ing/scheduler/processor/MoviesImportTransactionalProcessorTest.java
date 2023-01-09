package com.ing.scheduler.processor;

import com.ing.integration.dto.FilmwebResponse;
import com.ing.integration.dto.NetflixResponse;
import com.ing.model.MovieEntity;
import com.ing.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MoviesImportTransactionalProcessorTest {

    private MoviesImportTransactionalProcessor underTest;
    private MovieRepository movieRepository;

    @BeforeEach
    void init() {
        movieRepository = mock(MovieRepository.class);
        underTest = new MoviesImportTransactionalProcessor(movieRepository);
    }

    @Test
    void shouldMergeRatingsForMovies() {
        //given:
        MovieEntity predatorMovie = new MovieEntity();
        predatorMovie.setTitle("Predators");

        MovieEntity starWarsMovie = new MovieEntity();
        starWarsMovie.setTitle("Star Wars");

        Map<String, NetflixResponse.NetflixResponseContent> netflixMap = createNetflixMap();
        Map<String, FilmwebResponse.FilmwebResponseContent> ratingsMap = createFilmwebMap();

        when(movieRepository.findDistinctTitle()).thenReturn(Set.of("Predators", "Star Wars"));
        when(movieRepository.findByTitle("Predators")).thenReturn(Optional.of(predatorMovie));
        when(movieRepository.findByTitle("Star Wars")).thenReturn(Optional.of(starWarsMovie));
        //when:
        underTest.processRatings(netflixMap, ratingsMap);
        //then:
        verify(movieRepository, times(1)).findDistinctTitle();
        //- should create Interstellar in DB
        verifyCreated("Interstellar");
        //- should update predators and star wars
        verifyUpdated(predatorMovie);
        verifyUpdated(starWarsMovie);
    }

    private void verifyUpdated(MovieEntity movie) {
        verify(movieRepository, times(1)).findByTitle(movie.getTitle());
        verify(movieRepository, times(1)).save(movie);
    }

    private void verifyCreated(String title) {
        ArgumentCaptor<MovieEntity> movieEntityArgumentCaptor = ArgumentCaptor.forClass(MovieEntity.class);
        verify(movieRepository, times(3)).save(movieEntityArgumentCaptor.capture());
        MovieEntity savedMovie = movieEntityArgumentCaptor.getValue();
        assertEquals(title, savedMovie.getTitle());
    }

    private Map<String, FilmwebResponse.FilmwebResponseContent> createFilmwebMap() {
        Map<String, FilmwebResponse.FilmwebResponseContent> filmwebmap = new HashMap<>();
        filmwebmap.put("Star Wars", new FilmwebResponse.FilmwebResponseContent("Star Wars", 12346, 9.1));
        filmwebmap.put("Predators", new FilmwebResponse.FilmwebResponseContent("Predators", 12345, 9.2));
        filmwebmap.put("Interstellar", new FilmwebResponse.FilmwebResponseContent("Interstellar", 12347, 9.3));
        return filmwebmap;
    }

    private Map<String, NetflixResponse.NetflixResponseContent> createNetflixMap() {
        Map<String, NetflixResponse.NetflixResponseContent> netflixMap = new HashMap<>();
        netflixMap.put("Troy", new NetflixResponse.NetflixResponseContent(123L, "Troy", 2000, "Bajka o krolach"));
        netflixMap.put("Predators", new NetflixResponse.NetflixResponseContent(124L, "Predators", 2010, "Bajka o jaszczurkach zabijakach z kosmosu"));
        netflixMap.put("Interstellar", new NetflixResponse.NetflixResponseContent(125L, "Interstellar", 2014, "Matt Damon omal nie niszczy misji ratowania ludzkosci"));
        netflixMap.put("Star Wars", new NetflixResponse.NetflixResponseContent(126L, "Star Wars", 2018, "Jak zniszczyc trylogie"));
        return netflixMap;
    }

}