package com.ing.service;

import com.ing.exception.BestMovieNotExistsException;
import com.ing.exception.ExclusiveMovieNotExistsException;
import com.ing.model.RatingEntity;
import com.ing.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    @Transactional(readOnly = true)
    public RatingEntity findBestMovieExclusive(String db) {
        List<RatingEntity> exclusiveMovie = ratingRepository.findBestExclusiveRatingForDb(db, PageRequest.of(0, 1));
        if (exclusiveMovie.isEmpty()) {
            throw new ExclusiveMovieNotExistsException(db);
        }
        return exclusiveMovie.get(0);
    }

    @Transactional(readOnly = true)
    public RatingEntity findBestMovie(String db) {
        List<RatingEntity> movies = ratingRepository.findBestRatingForDb(db, PageRequest.of(0, 1));
        if (movies.isEmpty()) {
            throw new BestMovieNotExistsException(db);
        }
        return movies.get(0);
    }
}
