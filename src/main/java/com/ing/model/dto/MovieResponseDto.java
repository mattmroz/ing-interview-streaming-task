package com.ing.model.dto;

import com.ing.model.MovieEntity;
import com.ing.model.RatingEntity;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class MovieResponseDto {
    String title;
    List<MovieRatingResponseDto> ratings;

    public MovieResponseDto(MovieEntity movie) {
        this.title = movie.getTitle();
        this.ratings = movie.getRatings().stream().map(MovieRatingResponseDto::new).collect(Collectors.toList());
    }

    @Value
    public class MovieRatingResponseDto {
        String name;
        double rating;
        long userViews;

        public MovieRatingResponseDto(RatingEntity rating) {
            this.name = rating.getName();
            this.rating = rating.getRating();
            this.userViews = rating.getUserViews();
        }
    }
}
