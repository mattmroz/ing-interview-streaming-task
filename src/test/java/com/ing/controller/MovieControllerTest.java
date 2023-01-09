package com.ing.controller;

import com.ing.model.MovieEntity;
import com.ing.model.RatingEntity;
import com.ing.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void shouldFindMoviesByTitle() throws Exception {
        //given:
        createTestMovies();

        mockMvc.perform(get("/api/search?title=Wars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("Star Wars - part 1"))
                .andExpect(jsonPath("$.[1].title").value("Star Wars - part 2"))
                .andExpect(jsonPath("$.[2].title").value("Star Wars - part 3"))
                .andExpect(jsonPath("$.[3].title").value("Star Wars - part 4"));

        //clean:
        movieRepository.deleteAll();
    }

    private void createTestMovies() {
        createTestMovie("Interstellar", 8.0);
        createTestMovie("Star Wars - part 1", 7.0);
        createTestMovie("Star Wars - part 2", 7.5);
        createTestMovie("Star Wars - part 3", 7.9);
        createTestMovie("Star Wars - part 4", 8.9);
    }

    private void createTestMovie(String title, double rate) {
        MovieEntity movieEntity0 = new MovieEntity();
        movieEntity0.setTitle(title);

        RatingEntity ratingEntity0 = new RatingEntity();
        ratingEntity0.setMovie(movieEntity0);
        ratingEntity0.setName("netflix");
        ratingEntity0.setUserViews(35000);
        ratingEntity0.setRating(rate + 0.8);

        RatingEntity ratingEntity1 = new RatingEntity();
        ratingEntity1.setMovie(movieEntity0);
        ratingEntity1.setName("prime");
        ratingEntity1.setUserViews(45000);
        ratingEntity1.setRating(rate + 0.6);

        movieEntity0.setRatings(Set.of(ratingEntity0, ratingEntity1));

        movieRepository.saveAndFlush(movieEntity0);
    }
}