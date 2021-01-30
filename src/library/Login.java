package library;

import library.users.Lender;
import library.users.Librarian;
import library.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This Login program prints a welcome message, adds users to a list, get username from input and checks if it exists
 * in the usernames list. If so, the user can access the library.
 */

public class Login {
    private boolean loggedIn = false, isRunning = true;
    public List<User> users = new ArrayList<>();
    private User currentUser; // The logged in user.

    /**
     * The login constructor contains the while loop where everything happens inside.
     * It will end when boolean isRunning is false.
     */
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

        System.out.println("\u001B[33mPlease come back soon!\u001B[0m");
    }

    /**
     * Prints a welcome message.
     */
    private void printWelcomeMessage () {
        System.out.println("\u001B[33mWelcome to the library.\u001B[0m");
    }

    /**
     * Ask for username.
     *
     * @return if username exists.
     */
    private boolean askForUsername () {
        System.out.println("Please type your \u001B[32m" + "username" + "\u001B[0m to log in. Type " + "\u001B[32m" + "exit" + "\u001B[0m" + " to quit.");
        Scanner scanner = new Scanner(System.in);
        final String username = scanner.next();


        if (!checkUserNameForExit(username)) {
            return checkUser(username);
        } else {
            isRunning = false;
            return false;
        }
    }

    /**
     * Checks if username is "exit".
     *
     * @param username The user's name.
     * @return if username is "exit".
     */
    private boolean checkUserNameForExit (String username) {
        return username.equalsIgnoreCase("exit");
    }

    /**
     * Checks if the String is a username from the usernames list.
     *
     * @param userName The user's name.
     * @return if username exists in Username List.
     */
    private boolean checkUser (final String userName) {
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(userName)) {
                currentUser = user;
                System.out.println("Logged in as \u001B[34m" + userName + "\u001B[0m.");
                return true;
            }
        }

        System.out.println("User \u001B[31m" + userName + "\u001B[0m could not be found.");
        return false;
    }

    /**
     * Prints and listens for usable commands depending on {@link User} type:
     * {@link Librarian} or {@link Lender}.
     */
    private void getUserRequest () {
        if (currentUser instanceof Librarian)
            System.out.println("\u001B[33mWhat would you like to do? Pick an option.\u001B[0m\n" +
                    "1: Search for book title.\n" +
                    "2: Lend book.\n" +
                    "3: List your lent books.\n" +
                    "4: See list of lenders.\n" +
                    "5: Show time left lending for book.\n" +
                    "6: Add book to library." +
                    "7: See borrowed books." +
                    "8: Log out user.");
        else
            System.out.println("\u001B[33mWhat would you like to do? Pick an option.\u001B[0m\n" +
                    "1: Search for book title.\n" +
                    "2: Lend book.\n" +
                    "3: List your lent books.\n" +
                    "4: Show time left lending for book.\n" +
                    "5: Log out user.");

        Scanner scanner = new Scanner(System.in);

        int userRequest = 0;
        if (scanner.hasNextInt()) {
            userRequest = scanner.nextInt();
        }

        if (currentUser instanceof Librarian) {
            switch (userRequest) {
                case 1:
                    System.out.println("Searching for book title...");
                    //Library.getInstance().serchFoorBookTitle("title");
                    break;
                case 2:
                    System.out.println("Lending book...");
                    break;
                case 3:
                    System.out.println("List user's lent books...");
                    break;
                case 4:
                    System.out.println("See list of Lenders...");
                    //Library.getInstance().getLenderList(users); TEST// SANDRA
                    break;
                case 5:
                    System.out.println("Showing time left on lent book...");
                    break;
                case 6:
                    System.out.println("Add book to library...");
                    Library.getInstance().addBook();
                    break;
                case 7:
                    System.out.println("See list of borrowed books...");
                    Library.getInstance().checkLoanedBooks();
                    break;
                case 8:
                    System.out.println("Logging out " + currentUser.getName() + "...");
                    currentUser = null;
                    loggedIn = false;
                    break;
                default:
                    System.out.println("\u001B[31mThat is not an option.\u001B[0m");
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
                    Library.getInstance().lentBook(currentUser);
                    break;
                case 3:
                    System.out.println("List user's lent books...");
                    break;
                case 4:
                    System.out.println("Showing time left on lent book...");
                    break;
                case 5:
                    System.out.println("Logging out " + currentUser.getName() + "...");
                    currentUser = null;
                    loggedIn = false;
                    break;
                default:
                    System.out.println("\u001B[31mThat is not an option.\u001B[0m");
                    break;
            }
        }
    }

    /**
     * Adds new users to the usersList.
     */
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