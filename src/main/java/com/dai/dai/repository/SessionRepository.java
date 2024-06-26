package com.dai.dai.repository;

import com.dai.dai.entity.SessionEntity;
import com.dai.dai.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Integer> {

    @Override
    Optional<SessionEntity> findById(Integer integer);

    Optional<SessionEntity> findByUserEmail(String user_email);

    @Override
    void deleteById(Integer integer);

    void deleteByUserEmail(String userEmail);

}
