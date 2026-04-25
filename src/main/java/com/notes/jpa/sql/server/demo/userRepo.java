package com.notes.jpa.sql.server.demo;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface userRepo extends JpaRepository <User, Integer> {


    Optional<User> findByUsername(String username);
}
