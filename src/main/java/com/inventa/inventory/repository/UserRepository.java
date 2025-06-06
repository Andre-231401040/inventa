package com.inventa.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventa.inventory.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);
}
