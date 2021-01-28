import library.Library;
import library.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryApplication {
    private static boolean loggedIn = false, isRunning = true;
    public List<User> users = new ArrayList<>();
    // TODO: add/hardcode users when User class is done.

    public static void main(String[] args) {

        Library lib = new Library();
        lib.addStartBooks();
        lib.searchBookTitle();

        printWelcomeMessage();
        while(!loggedIn)
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

    private static void printWelcomeMessage() {
        System.out.println("Welcome to the library.");
    }

    private static boolean askForUsername() {
        System.out.println("Please type your username...");
        Scanner scanner = new Scanner(System.in);
        final String username = scanner.next();

        // TODO: Uncomment this
        /*for (User user: users) {
            if (username.equals(user.name)) {
                System.out.println("Welcome " + username + ".");
                return true;
            }
        }*/

        // FIXME: Remove this after User class is done.
        if (username.equals("admin")) {
            System.out.println("Welcome " + username + ".");
            return true;
        }

        System.out.println("No matching username found.");
        return false;
    }

    private static void getUserRequest() {
        // TODO: Implement switch for each user request.

        // this is where we ask for: search book, lend book, print books...
        System.out.println("What would you like to do?");
        Scanner scanner = new Scanner(System.in);
        String action = scanner.next();
        System.out.println(action);
    }
}
