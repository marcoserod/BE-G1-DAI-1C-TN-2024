package com.dai.dai.repository;

import com.dai.dai.entity.UserFavoriteEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavoriteEntity, Integer> {

    List<UserFavoriteEntity> findByUserId (Integer userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_favorites WHERE user_id = :userId AND film_id = :filmId", nativeQuery = true)
    void deleteByUserIdAndFilmId(@Param("userId") Integer userId, @Param("filmId") Integer filmId);
}
