package com.ing.scheduler.processor;

import com.ing.integration.dto.FilmwebResponse;
import com.ing.integration.dto.NetflixResponse;
import com.ing.model.MovieEntity;
import com.ing.model.RatingEntity;
import com.ing.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoviesImportTransactionalProcessor {

    private final MovieRepository movieRepository;

    @Transactional
    public void processRatings(Map<String, NetflixResponse.NetflixResponseContent> netflixMap,
                               Map<String, FilmwebResponse.FilmwebResponseContent> ratingsMap) {
        Set<String> existingTitles = movieRepository.findDistinctTitle();

        for (String title : netflixMap.keySet()) {
            log.debug("Processing title: {}", title);
            boolean isNew = !existingTitles.contains(title);
            FilmwebResponse.FilmwebResponseContent rating = ratingsMap.get(title);
            if (isNew && rating != null) {
                log.debug("New title found: {} with rating, storing in DB", title);
                movieRepository.save(createMovie(netflixMap.get(title), rating));
            } else if (!isNew && rating != null) {
                log.debug("Title {} already exists updating rating");
                updateRatingFromFilmWeb(netflixMap.get(title), rating);
            } else {
                log.warn("There is no rating for {} skipping import or update", title);
            }
        }

        Set<String> notExistingAnymoreTitles = existingTitles.stream()
                .filter(t -> !netflixMap.containsKey(t))
                .collect(Collectors.toSet());

        deleteMoviesThatNotExistsAnymore(notExistingAnymoreTitles);
    }

    private void deleteMoviesThatNotExistsAnymore(Set<String> notExistingAnymoreTitles) {
        log.debug("Deleting: {} movies that does not exist anymore", notExistingAnymoreTitles.size());
        movieRepository.deleteAllByTitleIn(notExistingAnymoreTitles);
    }


    private void updateRatingFromFilmWeb(NetflixResponse.NetflixResponseContent netflixResponseContent,
                                         FilmwebResponse.FilmwebResponseContent rating) {

        MovieEntity movieEntity = movieRepository.findByTitle(netflixResponseContent.getTitle())
                .orElseThrow(() -> new IllegalStateException("Movie with title " + netflixResponseContent.getTitle() + " not found!"));

        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setName("filmweb");
        ratingEntity.setMovie(movieEntity);
        ratingEntity.setRating(rating.getRating());
        ratingEntity.setUserViews(rating.getUserReviews());
        ratingEntity.setInternalId(netflixResponseContent.getNetflixid());

        movieEntity.getRatings().removeIf(r -> "filmweb".equals(r.getName()));
        movieEntity.getRatings().add(ratingEntity);

        movieRepository.save(movieEntity);

    }

    private MovieEntity createMovie(NetflixResponse.NetflixResponseContent netflixResponseContent,
                                    FilmwebResponse.FilmwebResponseContent rating) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setTitle(netflixResponseContent.getTitle());

        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setName("filmweb");
        ratingEntity.setMovie(movieEntity);
        ratingEntity.setRating(rating.getRating());
        ratingEntity.setUserViews(rating.getUserReviews());
        ratingEntity.setInternalId(netflixResponseContent.getNetflixid());

        movieEntity.setRatings(Set.of(ratingEntity));

        return movieEntity;
    }

}
