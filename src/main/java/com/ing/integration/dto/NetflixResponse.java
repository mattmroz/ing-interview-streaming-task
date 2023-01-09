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
public class NetflixResponse {

    private long totalPages;
    private List<NetflixResponseContent> results;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NetflixResponseContent {
        private long netflixid;
        private String title;
        private int year;
        private String synopsis;
    }
}
