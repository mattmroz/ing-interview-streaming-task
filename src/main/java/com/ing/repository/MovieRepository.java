package com.ing.repository;

import com.ing.model.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    @Query("select distinct m.title from MovieEntity m")
    Set<String> findDistinctTitle();

    void deleteAllByTitleIn(Set<String> notExistingAnymoreTitles);

    Optional<MovieEntity> findByTitle(String title);

    @Query("select distinct m from MovieEntity m left join fetch m.ratings where m.title like %?1% order by m.id")
    List<MovieEntity> findAllByTitleWithRatings(String title);
}
