package com.example.game.services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.game.models.LevelTime;
import com.example.game.repo.LevelTimeRepository;

@Service
public class LevelTimeService {
	
	@Autowired
    private LevelTimeRepository levelTimeRepository;

    public String recordLevelTime(LevelTime levelTime) {
        levelTimeRepository.save(levelTime);
        return "Level time recorded successfully";
    }

    public List<Object[]> getBestTimes(Long userId) {
        List<LevelTime> levelTimes = levelTimeRepository.findByUserId(userId);

        // Group by level and find the minimum time for each level
        Map<Integer, Optional<LevelTime>> bestTimes = levelTimes.stream()
                .collect(Collectors.groupingBy(LevelTime::getLevel,
                        Collectors.minBy(Comparator.comparingInt(LevelTime::getTime))));

        // Transform to a list of arrays containing level and best time
        return bestTimes.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(time -> new Object[]{time.getLevel(), time.getTime()})
                .collect(Collectors.toList());
    }

}
