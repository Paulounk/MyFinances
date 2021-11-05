package com.finances.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finances.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmailContainingIgnoreCase (String email);
	
	boolean existsByEmail(String email);
}
