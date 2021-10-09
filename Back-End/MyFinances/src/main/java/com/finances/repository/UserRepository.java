package com.finances.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finances.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmailContainingIgnoreCase (String email);
	
	boolean existsByEmail(String email);
}
