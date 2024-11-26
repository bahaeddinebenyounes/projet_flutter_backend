package com.example.game.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.game.models.LevelTime;

public interface LevelTimeRepository extends JpaRepository<LevelTime, Long>{
    List<LevelTime> findByUserId(Long userId);

}
