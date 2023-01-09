package com.ing.controller;

import com.ing.model.MovieEntity;
import com.ing.model.dto.MovieResponseDto;
import com.ing.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity findByTitle(@RequestParam(name = "title") String title) {
        List<MovieEntity> movies = movieService.findByTitle(title);
        return ResponseEntity.ok(movies.stream()
                .map(MovieResponseDto::new).collect(Collectors.toList()));
    }
}
