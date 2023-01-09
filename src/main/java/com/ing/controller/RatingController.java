package com.ing.controller;

import com.ing.model.RatingEntity;
import com.ing.model.dto.BestMovieExclusiveResponseDto;
import com.ing.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/bestMovieExclusive")
    public ResponseEntity findBestMovieExclusive(@RequestParam(name = "db") String db){
        RatingEntity bestMovie = ratingService.findBestMovieExclusive(db);
        return ResponseEntity.ok(new BestMovieExclusiveResponseDto(bestMovie));
    }

    @GetMapping("/bestMovie")
    public ResponseEntity findBestMovie(@RequestParam(name = "db") String db){
        RatingEntity bestMovie = ratingService.findBestMovie(db);
        return ResponseEntity.ok(new BestMovieExclusiveResponseDto(bestMovie));
    }
}
