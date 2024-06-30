package com.dai.dai.repository;

import com.dai.dai.entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Override
    Optional<UserEntity> findById(Integer integer);

    @Override
    void flush();

    @Override
    void delete(UserEntity entity);

    UserEntity findByNickname(String nickname);

    UserEntity findByEmail(String Email);

    @Override
    void deleteById(Integer userId);


}
