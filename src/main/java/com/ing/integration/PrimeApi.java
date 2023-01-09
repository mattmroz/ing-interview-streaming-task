package com.ing.integration;

import com.ing.integration.dto.PrimeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "primeApi", url = "${integration.prime.url}")
public interface PrimeApi {
    @RequestMapping(method = RequestMethod.GET, value = "/prime/api/movies?offset={offset}&limit={limit}")
    PrimeResponse getPrimePage(@PathVariable long offset, @PathVariable long limit);
}
