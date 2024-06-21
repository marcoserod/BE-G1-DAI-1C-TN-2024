package com.dai.dai.repository;

import com.dai.dai.entity.UserEntity;
import com.dai.dai.entity.UserMovieRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMovieRatingRepository extends JpaRepository<UserMovieRatingEntity, Integer> {

    @Override
    <S extends UserMovieRatingEntity> S save(S entity);

    @Query("SELECT ur FROM UserMovieRatingEntity ur WHERE ur.user_id  = :userId AND ur.movie_id  = :movieId")
    Optional<UserMovieRatingEntity> getUserMovieRatingEntity(@Param("userId") Long userId, @Param("movieId") Long movieId);
}
