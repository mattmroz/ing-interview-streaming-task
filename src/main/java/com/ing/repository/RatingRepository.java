package com.ing.repository;

import com.ing.model.RatingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    @Query("select r from RatingEntity r join fetch r.movie where r.name = ?1 and 1 = (select count(pr) from RatingEntity pr where pr.movie.id = r.movie.id)" +
            "order by r.rating desc ")
    List<RatingEntity> findBestExclusiveRatingForDb(String db, Pageable pageable);

    @Query("select r from RatingEntity r where r.name = ?1 order by r.rating desc")
    List<RatingEntity> findBestRatingForDb(String db, Pageable pageable);
}
