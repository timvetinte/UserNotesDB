package com.notes.jpa.sql.server.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface notesRepo extends JpaRepository<Notes, Integer> {
    List<Notes> findByPersonID(Integer personID);
}