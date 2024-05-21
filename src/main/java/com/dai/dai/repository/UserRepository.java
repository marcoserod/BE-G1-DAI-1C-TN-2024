package com.dai.dai.repository;

import com.dai.dai.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Override
    Optional<UserEntity> findById(Integer integer);

    UserEntity findByNickname(String nickname);

    UserEntity findByEmail(String Email);
}
