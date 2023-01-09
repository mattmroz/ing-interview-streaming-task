package com.ing.integration;

import com.ing.integration.dto.NetflixResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "netflixApi", url = "${integration.netflix.url}")
public interface NetflixApi {

    @RequestMapping(method = RequestMethod.GET, value = "/netflix/api/movies?page={page}")
    NetflixResponse getNetflixPage(@PathVariable long page);
}
