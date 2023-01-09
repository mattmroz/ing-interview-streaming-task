package com.ing.scheduler.processor;

import com.ing.integration.FilmwebApi;
import com.ing.integration.NetflixApi;
import com.ing.integration.dto.FilmwebResponse;
import com.ing.integration.dto.NetflixResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoviesImportProcessor {

    private final NetflixApi netflixApi;
    private final FilmwebApi filmwebApi;

    private final MoviesImportTransactionalProcessor moviesImportTransactionalProcessor;

    public void importMovies() {
        log.info("Importing movies from netflix");
        Set<NetflixResponse.NetflixResponseContent> allNetflixMovies = findAllNetflixMovies();
        log.info("Found {} in netflix db", allNetflixMovies.size());
        log.info("Importing ratings from filmweb");
        Set<FilmwebResponse.FilmwebResponseContent> allRatingsFromFilmWeb = findAllRatingsFromFilmweb();
        log.info("Found {} ratings in filmweb db", allRatingsFromFilmWeb.size());

        Map<String, FilmwebResponse.FilmwebResponseContent> ratingsMap = allRatingsFromFilmWeb.stream()
                        .collect(Collectors.toMap(FilmwebResponse.FilmwebResponseContent::getTitle, Function.identity()));
        Map<String, NetflixResponse.NetflixResponseContent> netflixMap = allNetflixMovies.stream()
                        .collect(Collectors.toMap(NetflixResponse.NetflixResponseContent::getTitle, Function.identity()));
        moviesImportTransactionalProcessor.processRatings(netflixMap, ratingsMap);
    }

    private Set<FilmwebResponse.FilmwebResponseContent> findAllRatingsFromFilmweb() {
        Set<FilmwebResponse.FilmwebResponseContent> content = new HashSet<>();

        long currentPage = 1;
        FilmwebResponse response = filmwebApi.getRatings(currentPage);
        content.addAll(response.getResults());
        long totalPages = response.getTotalPages();
        for (long i = 2; i <= totalPages; i++) {
            content.addAll(filmwebApi.getRatings(currentPage).getResults());
        }

        return content;
    }

    private Set<NetflixResponse.NetflixResponseContent> findAllNetflixMovies() {
        Set<NetflixResponse.NetflixResponseContent> content = new HashSet<>();

        long currentPage = 1;
        NetflixResponse response = netflixApi.getNetflixPage(currentPage);
        content.addAll(response.getResults());
        long totalPages = response.getTotalPages();
        for (long i = 2; i <= totalPages; i++) {
            content.addAll(netflixApi.getNetflixPage(i).getResults());
        }

        return content;
    }
}
