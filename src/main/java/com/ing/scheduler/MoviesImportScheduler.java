package com.ing.scheduler;

import com.ing.scheduler.processor.MoviesImportProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoviesImportScheduler {

    private final MoviesImportProcessor moviesImportProcessor;

    @Scheduled(cron = "${movies.scheduler.cron}")
    public void loadMovies() {
        log.info("Starting loading movies");
        moviesImportProcessor.importMovies();
    }
}
