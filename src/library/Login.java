package library;

import library.users.Lender;
import library.users.Librarian;
import library.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Login {
    private boolean loggedIn = false, isRunning = true;
    public List<User> users = new ArrayList<>();

    public Login(){
        printWelcomeMessage();
        addLibraryUsers();

        while(!loggedIn && isRunning)
            loggedIn = askForUsername();

        if (loggedIn) {
            Library library = new Library();

            while (isRunning) {
                // ask what to do...
                getUserRequest();
            }

            loggedIn = false;
        }

        System.out.println("Thank you for visiting the Library. Please come back. We love you!");
    }

    private void printWelcomeMessage() {
        System.out.println("Welcome to the library.");
    }

    private boolean askForUsername() {
        System.out.println("Please type your username... or type 'exit' to quit.");
        Scanner scanner = new Scanner(System.in);
        final String username = scanner.next();


        if (!checkUserNameForExit(username)) {
            return checkUser(username);
        } else {
            isRunning = false;
            return false;
        }
    }

    private boolean checkUserNameForExit(String username) {
        if (username.equalsIgnoreCase("exit")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkUser(final String userName) {
        for (User user: users) {
            if (user.getName().equalsIgnoreCase(userName)) {
                System.out.println("Logged in as " + userName);
                return true;
            }
        }

        System.out.println("User " + userName + " could not be found.");
        return false;
    }

    private void getUserRequest() {
        // TODO: Implement switch for each user request. a

        // this is where we ask for: search book, lend book, print books...
        System.out.println("What would you like to do?");
        Scanner scanner = new Scanner(System.in);
        final String action = scanner.next();
        System.out.println(action);
    }

    private void addLibraryUsers() {
        // Librarians
        users.add(new Librarian("Marcel"));
        users.add(new Librarian("Johan"));

        // Lenders
        users.add(new Lender("Adam"));
        users.add(new Lender("Amin"));
        users.add(new Lender("Annika"));
        users.add(new Lender("Christian"));
        users.add(new Lender("Sandra"));
    }
}