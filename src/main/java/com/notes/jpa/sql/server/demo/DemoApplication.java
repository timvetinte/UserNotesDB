package com.notes.jpa.sql.server.demo;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    userRepo uRepo;
	@Autowired
	Menu menu;


    @Override
    public void run(String... args) {
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
