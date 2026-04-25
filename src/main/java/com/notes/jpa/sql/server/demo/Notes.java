package com.notes.jpa.sql.server.demo;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notes")
@Data
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noteid")
    Integer id;
    Integer personID;
    String notetext;
}

