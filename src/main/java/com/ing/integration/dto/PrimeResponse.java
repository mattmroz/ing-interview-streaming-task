package com.ing.integration.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrimeResponse {
    private long total;
    private List<PrimeResponseContent> results;

    @Getter
    @Setter
    public class PrimeResponseContent {
        private long primeid;
        private String title;
        private int year;
        private String synopsis;
    }
}
