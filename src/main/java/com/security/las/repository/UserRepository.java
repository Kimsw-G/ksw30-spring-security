package com.security.las.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.las.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

    public User findByUsername(String username);
}
