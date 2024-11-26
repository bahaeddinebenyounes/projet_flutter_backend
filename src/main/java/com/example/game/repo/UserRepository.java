package com.example.game.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.game.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
    Optional<User> findByUsername(String username);

}
