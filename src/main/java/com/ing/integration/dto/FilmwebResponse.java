package com.ing.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmwebResponse {
    private long totalPages;
    private List<FilmwebResponseContent> results;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilmwebResponseContent {
        private String title;
        private long userReviews;
        private double rating;
    }
}
