package com.example.game.contorllers;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.game.models.LevelTime;
import com.example.game.models.User;
import com.example.game.repo.LevelTimeRepository;
import com.example.game.repo.UserRepository;
import com.example.game.services.LevelTimeService;

@RestController
@RequestMapping("/api/level-times")
public class LevelTimeController {

	@Autowired
    private LevelTimeRepository levelTimeRepository;
	
	@Autowired
    private LevelTimeService levelTimeService;
	
	@Autowired
    private UserRepository userRepository;

    // Record Level Time
	@PostMapping("/record")
	public ResponseEntity<String> recordLevelTime(@RequestBody Map<String, Object> request) {
	    try {
	        // Log incoming request for debugging
	        System.out.println("Received request: " + request);

	        // Validate and parse input data
	        if (!request.containsKey("user_id") || !request.containsKey("level") || !request.containsKey("time")) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields: user_id, level, or time.");
	        }

	        Long userId;
	        int level;
	        int time;

	        try {
	            userId = Long.valueOf(request.get("user_id").toString());
	            level = Integer.parseInt(request.get("level").toString());
	            time = Integer.parseInt(request.get("time").toString());
	        } catch (NumberFormatException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data types for user_id, level, or time.");
	        }

	        // Process and save the level time
	        Optional<User> userOptional = userRepository.findById(userId);
	        if (userOptional.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user_id: user does not exist.");
	        }

	        LevelTime levelTime = new LevelTime();
	        levelTime.setUser(userOptional.get());
	        levelTime.setLevel(level);
	        levelTime.setTime(time);

	        levelTimeService.recordLevelTime(levelTime);

	        return ResponseEntity.status(HttpStatus.CREATED).body("Level time recorded successfully");
	    } catch (Exception e) {
	        // Log unexpected errors for debugging
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
	    }
	}



    // Get Best Times
    @GetMapping("/best/{userId}")
    public List<Object[]> getBestTimes(@PathVariable Long userId) {
        return levelTimeRepository.findByUserId(userId)
                .stream()
                .collect(Collectors.groupingBy(LevelTime::getLevel,
                        Collectors.minBy(Comparator.comparingInt(LevelTime::getTime))))
                .values()
                .stream()
                .map(Optional::get)
                .map(l -> new Object[]{l.getLevel(), l.getTime()})
                .toList();
    }
}
