package com.ing.integration;

import com.ing.integration.dto.FilmwebResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "filmwebApi", url = "${integration.filmweb.url}")
public interface FilmwebApi {

    @RequestMapping(method = RequestMethod.GET, value = "/filmweb/api/ratings?page={page}")
    FilmwebResponse getRatings(@PathVariable long page);
}

