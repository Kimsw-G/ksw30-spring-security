package com.security.las.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.las.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{

    public User findByUsername(String username);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
