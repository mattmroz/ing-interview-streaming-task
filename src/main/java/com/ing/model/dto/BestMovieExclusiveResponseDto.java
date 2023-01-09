package com.ing.model.dto;

import com.ing.model.RatingEntity;
import lombok.Value;

@Value
public class BestMovieExclusiveResponseDto {
    String title;
    BestMovieRatingResponseDto rating;
    BestMovieStreamingServiceResponseDto streamingService;

    public BestMovieExclusiveResponseDto(RatingEntity bestMovie) {
        this.title = bestMovie.getMovie().getTitle();
        this.rating = new BestMovieRatingResponseDto(bestMovie);
        this.streamingService = new BestMovieStreamingServiceResponseDto(bestMovie);
    }

    @Value
    public class BestMovieRatingResponseDto {
        String name;
        double rating;
        long userReviews;

        public BestMovieRatingResponseDto(RatingEntity bestMovie) {
            this.name = bestMovie.getName();
            this.rating = bestMovie.getRating();
            this.userReviews = bestMovie.getUserViews();
        }
    }

    @Value
    public class BestMovieStreamingServiceResponseDto {
        String name;
        long movieId;

        public BestMovieStreamingServiceResponseDto(RatingEntity bestMovie) {
            this.name = bestMovie.getMovie().getTitle();
            this.movieId = bestMovie.getInternalId();
        }
    }
}