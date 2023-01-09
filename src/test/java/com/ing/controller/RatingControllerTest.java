package com.ing.controller;

import com.ing.model.MovieEntity;
import com.ing.model.RatingEntity;
import com.ing.repository.MovieRepository;
import com.ing.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void shouldFindBestExclusiveMovie() throws Exception {
        //given
        createMoviesWithNonExclusiveProviders();
        //when
        mockMvc.perform(get("/api/bestMovieExclusive?db=netflix"))
                .andDo(print())
                .andExpect(jsonPath("$.title").value("Exclusive"))
                .andExpect(status().isOk());
        //clean:
        movieRepository.deleteAll();
    }

    @Test
    void shouldFindBestMovie() throws Exception {
        //given
        createMoviesWithNonExclusiveProviders();
        //when
        mockMvc.perform(get("/api/bestMovie?db=netflix"))
                .andDo(print())
                .andExpect(jsonPath("$.title").value("Exclusive-NOT"))
                .andExpect(status().isOk());
        //clean:
        movieRepository.deleteAll();
    }

    private void createMoviesWithNonExclusiveProviders() {

        MovieEntity movieEntity0 = new MovieEntity();
        movieEntity0.setTitle("Exclusive but not best");

        RatingEntity ratingEntity0 = new RatingEntity();
        ratingEntity0.setMovie(movieEntity0);
        ratingEntity0.setName("netflix");
        ratingEntity0.setUserViews(3500);
        ratingEntity0.setRating(8.0);

        movieEntity0.setRatings(Set.of(ratingEntity0));

        movieRepository.saveAndFlush(movieEntity0);

        MovieEntity movieEntity1 = new MovieEntity();
        movieEntity1.setTitle("Exclusive");

        RatingEntity ratingEntity1 = new RatingEntity();
        ratingEntity1.setMovie(movieEntity1);
        ratingEntity1.setName("netflix");
        ratingEntity1.setUserViews(3500);
        ratingEntity1.setRating(9.0);

        movieEntity1.setRatings(Set.of(ratingEntity1));

        movieRepository.saveAndFlush(movieEntity1);

        MovieEntity movieEntity2 = new MovieEntity();
        movieEntity2.setTitle("Exclusive-NOT");

        RatingEntity ratingEntity2 = new RatingEntity();
        ratingEntity2.setMovie(movieEntity2);
        ratingEntity2.setName("netflix");
        ratingEntity2.setUserViews(3500);
        ratingEntity2.setRating(9.5);

        RatingEntity ratingEntity3 = new RatingEntity();
        ratingEntity3.setMovie(movieEntity2);
        ratingEntity3.setName("prime");
        ratingEntity3.setUserViews(3500);
        ratingEntity3.setRating(9.5);

        movieEntity2.setRatings(Set.of(ratingEntity3, ratingEntity2));

        movieRepository.saveAndFlush(movieEntity2);
    }
}