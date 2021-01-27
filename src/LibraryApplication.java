import library.Library;
import library.users.Lender;
import library.users.Librarian;
import library.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryApplication {
    private static boolean loggedIn = false, isRunning = true;
    public static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
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

    private static void printWelcomeMessage() {
        System.out.println("Welcome to the library.");
    }

    private static boolean askForUsername() {
        System.out.println("Please type your username... or type exit to quit.");
        Scanner scanner = new Scanner(System.in);
        final String username = scanner.next();

        if (username.equalsIgnoreCase("exit")) {
            isRunning = false;
            return false;
        } else {
            for (User user : users) {
                if (username.equalsIgnoreCase(user.getName())) {
                    System.out.println("Welcome " + username + ".");
                    return true;
                }
            }
        }

        System.out.println("No matching username found.");
        return false;
    }

    private static void getUserRequest() {
        // TODO: Implement switch for each user request.

        // this is where we ask for: search book, lend book, print books...
        System.out.println("What would you like to do?");
        Scanner scanner = new Scanner(System.in);
        final String action = scanner.next();
        System.out.println(action);
    }

    private static void addLibraryUsers() {
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
