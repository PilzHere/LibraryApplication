import library.Library;
import library.Login;
import library.users.Lender;
import library.users.Librarian;
import library.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryApplication {
    private static boolean loggedIn = false, isRunning = true;
    private Login login = new Login();
    public static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        Login login = new Login();
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

    private static Boolean askForUsername() {
        System.out.println("Please type your username...");
        Scanner scanner = new Scanner(System.in);
        final String username = scanner.next();
        return true;
       /*
        if(login.checkUser(username))
            return true;
        else
            return false;*/
    }

    private static void getUserRequest() {
        // TODO: Implement switch for each user request. a

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