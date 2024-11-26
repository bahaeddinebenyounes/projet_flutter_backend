package com.example.game.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.game.models.User;
import com.example.game.repo.UserRepository;

@Service
public class UserService {
	
	   @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private BCryptPasswordEncoder passwordEncoder;

	    public String register(User user) {
	        // Check if the username already exists
	        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
	            return "Username already exists!";
	        }
	        // Hash the password and save the user
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        userRepository.save(user);
	        return "User registered successfully";
	    }

	    public boolean login(String username, String password) {
	        Optional<User> user = userRepository.findByUsername(username);
	        if (user.isPresent()) {
	            return passwordEncoder.matches(password, user.get().getPassword());
	        }
	        return false;
	    }

}
