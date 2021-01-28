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
    private User currentUser;

    public Login () {
        printWelcomeMessage();
        addLibraryUsers();

        while (isRunning) {
            if (!loggedIn) {
                loggedIn = askForUsername();
            }

            if (loggedIn) {
                getUserRequest();
            }
        }

        System.out.println("Thank you for visiting the Library. Please come back. We have hotdogs and carbonated soda!");
    }

    private void printWelcomeMessage () {
        System.out.println("Welcome to the library.");
    }

    private boolean askForUsername () {
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

    private boolean checkUserNameForExit (String username) {
        return username.equalsIgnoreCase("exit");
    }

    private boolean checkUser (final String userName) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(userName)) {
                currentUser = user;
                System.out.println("Logged in as " + userName + ".");
                return true;
            }
        }

        System.out.println("User '" + userName + "' could not be found.");
        return false;
    }

    private void getUserRequest () {
        System.out.println("What would you like to do?\n1: Search for book title.\n2: Lend book.\n3: List your lended books.\n4: Show time left lending for book.\n5: Log out user.");
        Scanner scanner = new Scanner(System.in);

        int userRequest = 0;
        if (scanner.hasNextInt()) {
            userRequest = scanner.nextInt();
        }

        if (currentUser instanceof Librarian) {
            switch (userRequest) {
                case 1:
                    System.out.println("Searching for book title...");
                    // FIXME Example usage
                    //Library.getInstance().serchFoorBookTitle("title");
                    break;
                case 2:
                    System.out.println("Lending book...");
                    break;
                case 3:
                    System.out.println("List user's lended books...");
                    break;
                case 4:
                    System.out.println("Showing time left on lended book...");
                    break;
                case 5:
                    System.out.println("Logging out " + currentUser.getName() + "...");
                    currentUser = null;
                    loggedIn = false;
                    break;
                default:
                    System.out.println("That is not a known command.");
                    break;
            }
        } else if (currentUser instanceof Lender) {
            switch (userRequest) {
                case 1:
                    System.out.println("Searching for book title...");
                    //Library.getInstance().serchFoorBookTitle("title"); EXAMPLE
                    break;
                case 2:
                    System.out.println("Lending book...");
                    break;
                case 3:
                    System.out.println("List user's lended books...");
                    break;
                case 4:
                    System.out.println("Showing time left on lended book...");
                    break;
                case 5:
                    System.out.println("Logging out " + currentUser.getName() + "...");
                    currentUser = null;
                    loggedIn = false;
                    break;
                default:

                    System.out.println("That is not a known command.");
                    break;
            }
        }
    }

    private void addLibraryUsers () {
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