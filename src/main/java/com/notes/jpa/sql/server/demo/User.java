package com.notes.jpa.sql.server.demo;


import jakarta.persistence.*;
import lombok.Data;

@Entity
    @Table(name = "users")
    @Data
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "userID")
        Integer id;
        String username;
        String password;
        String role;
    }

