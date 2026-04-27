package com.notes.jpa.sql.server.demo;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class Auth{

    @Autowired
    userRepo repo;

    public void register() {
        Scanner scanner = new Scanner(System.in);

        String username;
        String password;

        User newUser = new User();

        while(true) {
            System.out.println("Enter username:");
            username = scanner.nextLine();
            if (!username.isEmpty()){
                break;
            } else {
                System.out.println("Username can not be blank.");
            }
        }

        Optional<User> optionalUser = repo.findByUsername(username);

        if (optionalUser.isPresent()) {

            System.out.println("User already exists");

        } else {
            newUser.setUsername(username);

            while(true) {
                System.out.println("Enter a password:");
                password = scanner.nextLine();
                if (!password.isEmpty()){
                    break;
                } else {
                    System.out.println("Password cant be blank");
                }
            }

            System.out.println("Enter password again");

            String password2 = scanner.nextLine();

            if(password.equals(password2)) {

                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                newUser.setPassword(hashedPassword);
                System.out.println("User created " + newUser.getUsername());
                newUser.setRole("USER");
                repo.save(newUser);

            } else {
                System.out.println("Passwords did not match try again");
            }

        }
    }

    public User login() {
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;

        System.out.println("Enter username:");
        String username = scanner.nextLine();

        Optional<User> optionalUser = repo.findByUsername(username);

            if (optionalUser.isPresent()){
                currentUser = optionalUser.get();

                System.out.println("User found");
                System.out.println("Enter Password:");

                String password = scanner.nextLine();

                if (BCrypt.checkpw(password, currentUser.getPassword())){
                    System.out.println("Correct Password!!!");
                } else {
                    System.out.println("Incorrect password");
                }

            } else {
                System.out.println("User does not exist");
            }
            return currentUser;
        }

}
