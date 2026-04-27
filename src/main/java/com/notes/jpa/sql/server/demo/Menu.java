package com.notes.jpa.sql.server.demo;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Scanner;

@RestController
@SpringBootApplication
public class Menu {

    @Autowired
    notesRepo nRepo;
    @Autowired
    userRepo uRepo;
    @Autowired
    Auth auth;


    public enum State {
        MAIN_MENU, USER_MENU, VIEW_NOTES, SELECT_NOTE, CREATE_NOTE, EDIT_NOTE, CHANGE_PASSWORD, LOG_IN, REGISTER
    }

    State current = State.MAIN_MENU;

    public void startMenu(User currentUser) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Current user: " + currentUser.getUsername());
        System.out.println("1. to View Notes 2. to Change Password 3. to Log Out");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> current = State.VIEW_NOTES;
            case 2 -> changePassword(currentUser);
            case 3 -> {
                System.out.println("Logged out");
                current = State.MAIN_MENU;
            }

        }


    }

    public User mainMenu() {
        User currentUser;

        while (true) {
            System.out.println("1. Login \n2. Register \n3. Quit");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    currentUser = auth.login();
                    if (currentUser != null) {
                        current = State.USER_MENU;
                        return currentUser;
                    }
                }
                case "2" -> auth.register();
                case "3" -> System.exit(0);

            }
        }
    }


    public ArrayList<Notes> getNotesFromUser(User currentUser) {
        ArrayList<Notes> personNotes = new ArrayList<>();
        personNotes.addAll(nRepo.findByPersonID(currentUser.getId()));
        return personNotes;
    }

    public ArrayList<Notes> getAllNotes() {
        ArrayList<Notes> personNotes = new ArrayList<>();
        personNotes.addAll(nRepo.findAll());
        return personNotes;
    }

    public void displayNotes(ArrayList<Notes> noteList) {
        Integer index = 1;
        if (!noteList.isEmpty()) {
            for (Notes n : noteList) {
                System.out.println("Note n" + index + ": " + n.notetext);
                System.out.println();
                index++;
            }
        } else {
            System.out.println("No Notes");
        }


    }

    public Notes displayNotesRoleCheck(User currentUser) {
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            displayNotes(getAllNotes());
            return selectNote(currentUser);
        } else {
            displayNotes(getNotesFromUser(currentUser));
            return selectNote(currentUser);
        }
    }

    public void changePassword(User currentUser) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter new password: ");
            String password1 = scanner.nextLine();
            System.out.print("Enter password again: ");
            String password2 = scanner.nextLine();
            if (password1.equals(password2)) {

                String hashedPassword = BCrypt.hashpw(password1, BCrypt.gensalt());
                currentUser.setPassword(hashedPassword);

                uRepo.save(currentUser);
                System.out.println("Password set!");
                break;
            } else {
                System.out.println("Passwords did not match, try again");
            }
        }
        startMenu(currentUser);
    }

    public Notes selectNote(User currentUser) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Notes> noteList;

        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            noteList = getAllNotes();
        } else {
            noteList = getNotesFromUser(currentUser);
        }

        System.out.println("Enter note number to edit or " + (noteList.size() + 1) + ". to Add Note " + (noteList.size() + 2) + ". to Exit");
        int choice = scanner.nextInt();

        if (choice == noteList.size() + 1) {
            createNote(currentUser);

        } else if (choice == noteList.size() + 2) {
            startMenu(currentUser);
        } else if (choice <= noteList.size() && choice > 0) {
            Notes currentNote;
            currentNote = noteList.get(choice - 1);
            current = State.EDIT_NOTE;
            return currentNote;

        } else {
            System.out.println("Invalid choice!");
        }
        return null;
    }

    public void createNote(User currentUser) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter note text: ");
        String noteText = scanner.nextLine();
        Notes note = new Notes();
        note.setNotetext(noteText);
        note.setPersonID(currentUser.id);
        System.out.println(currentUser.id);
        System.out.println(note.personID);
        nRepo.save(note);
        System.out.println("Note Saved!");
        current = State.USER_MENU;
    }

    public void editNote(Notes currentNote) {
        System.out.println(currentNote.personID);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Rewrite note: ");
        String noteText = scanner.nextLine();
        System.out.println("1. Save Note 2. Discard Changes 3. Delete Note");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                currentNote.setNotetext(noteText);
                nRepo.save(currentNote);
            }
            case 2 -> System.out.println("Changes discarded");
            case 3 -> nRepo.delete(currentNote);

        }
        current = State.VIEW_NOTES;
    }

    public void startStateMachine() {
        User currentUser = new User();
        Notes currentNote = new Notes();

        while (true) {
            switch (current) {
                case MAIN_MENU -> currentUser = mainMenu();
                case USER_MENU -> startMenu(currentUser);
                case VIEW_NOTES -> currentNote = displayNotesRoleCheck(currentUser);
                case SELECT_NOTE -> currentNote = selectNote(currentUser);
                case EDIT_NOTE -> editNote(currentNote);
                case CREATE_NOTE -> createNote(currentUser);
                case CHANGE_PASSWORD -> changePassword(currentUser);

            }
        }
    }


}
