package com.ing.service;

import com.ing.model.MovieEntity;
import com.ing.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    @Transactional(readOnly = true)
    public List<MovieEntity> findByTitle(String title) {
        return movieRepository.findAllByTitleWithRatings(title);
    }
}
