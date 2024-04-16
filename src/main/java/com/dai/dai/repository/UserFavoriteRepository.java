package com.dai.dai.repository;

import com.dai.dai.entity.UserFavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavoriteEntity, Integer> {

    List<UserFavoriteEntity> findByUserId (Integer userId);
}
