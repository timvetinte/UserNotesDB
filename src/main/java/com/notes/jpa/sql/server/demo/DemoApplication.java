package com.notes.jpa.sql.server.demo;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    userRepo uRepo;
	@Autowired
	notesRepo nRepo;
    @Autowired
    Auth auth;
	@Autowired
	Menu menu;

    static boolean running = true;

    User currentUser = null;


    @GetMapping("testInquiry")
    public String testInq() {
        return uRepo.findAll().stream().toList().toString();
    }

    @GetMapping("testInsert")
    public String testInsert() {
        User u = new User();

        u.setUsername("timgru2000");
        u.setPassword("GangsterSquad");
        u.setRole("admin");
        return uRepo.save(u).toString();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.print("-----");



        if (uRepo.findByUsername("ADMIN").isEmpty()) {

            User admin = new User();
            admin.setUsername("ADMIN");

            String hashedPassword = BCrypt.hashpw("Admin123", BCrypt.gensalt());
            admin.setPassword(hashedPassword);

            admin.setRole("admin");
            uRepo.save(admin);
        }

        System.out.println("------");
    menu.startStateMachine();

    }


    static void main(String[] args) {
        System.out.print("---");
            SpringApplication.run(DemoApplication.class, args);
    }
}
