package library;

import library.books.Book;
import library.users.Lender;
import library.users.Librarian;
import library.users.User;
import library.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
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
     * It will end when boolean isRunning is false. a
     */
    public Login() {
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
    private void printWelcomeMessage() {
        System.out.println("\u001B[33mWelcome to the library.\u001B[0m");
    }

    /**
     * Ask for username.
     *
     * @return if username exists.
     */
    private boolean askForUsername() {
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
    private boolean checkUserNameForExit(String username) {
        return username.equalsIgnoreCase("exit");
    }

    /**
     * Checks if the String is a username from the usernames list.
     *
     * @param userName The user's name.
     * @return if username exists in Username List.
     */
    private boolean checkUser(final String userName) {
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
    private void getUserRequest() {
        Library.getInstance().bookCollection = FileUtils.checkIfFilesExists(Library.getInstance().bookCollection);
        //System.out.println("What would you like to do?\n1: Search for book title.\n2: Lend book.\n3: List your lended books.\n4: See list of lenders. \n5: Search for Lender and view lended books.\n6: Show time left lending for book.\n7: Log out user.");

        if (currentUser instanceof Librarian)
            System.out.println("\u001B[33mWhat would you like to do? Pick an option.\u001B[0m\n" +
                    "1: Search for books.\n" +
                    "2: View all books in collection.\n" +
                    "3: List your lent books.\n" +
                    "4: See list of lenders.\n" +
                    "5: Search lender and view lended books.\n" +
                    "6: Show time left lending for book.\n" +
                    "7: Add book to library.\n" +
                    "8: Remove book from collection.\n" +
                    "9: See borrowed books.\n" +
                    "10: Log out user.");
        else
            System.out.println("\u001B[33mWhat would you like to do? Pick an option.\u001B[0m\n" +
                    "1: Search for books.\n" +
                    "2: Lend book.\n" +
                    "3: List your lent books.\n" +
                    "4: Show time left lending for book.\n" +
                    "5: View all books in the library.\n" +
                    "6: Log out user.");

        Scanner scanner = new Scanner(System.in);

        int userRequest = 0;
        if (scanner.hasNextInt()) {
            userRequest = scanner.nextInt();
        }

        if (currentUser instanceof Librarian) {
            switch (userRequest) {
                case 1:
                    bookSearch();
                    break;
                case 2:
                    System.out.println("View all books in collection");
                    Library.getInstance().displayBookCollection();
                    break;
                case 3:
                    System.out.println("List user's lent books...");
                    break;
                case 4:
                    System.out.println("See list of Lenders...");
                    Library.getInstance().getLenderList(users);
                    break;
                case 5:
                    System.out.println("Search for Lender and view lended books...");
                    Library.getInstance().searchForLender(users);
                    break;
                case 6:
                    System.out.println("Showing time left on lended book...");
                    break;
                case 7:
                    System.out.println("Add book to library...");
                    Library.getInstance().addBook();
                    break;
                case 8:
                    System.out.println("Remove book from collection...");
                    Library.getInstance().removeBook();
                    break;
                case 9:
                    System.out.println("See list of borrowed books...");
                    Library.getInstance().checkLoanedBooks();
                    break;
                case 10:
                    System.out.println("Logging out " + currentUser.getName() + "...");
                    currentUser = null;
                    loggedIn = false;
                    FileUtils.saveAtLogout(Library.getInstance().bookCollection);
                    break;
                default:
                    System.out.println("\u001B[31mThat is not an option.\u001B[0m");
                    break;
            }
        } else if (currentUser instanceof Lender) {
            switch (userRequest) {
                case 1:
                    bookSearch();
                    break;
                case 2:
                    System.out.println("Lending book...");
                    Library.getInstance().lendBooks(currentUser);
                    break;
                case 3:
                    System.out.println("List user's lended books...");
                    Library.getInstance().booksBorrowed(currentUser);
                    break;
                case 4:
                    System.out.println("Showing time left on lent book...");
                    break;
                case 5:
                    bookList();
                    break;
                case 6:
                    System.out.println("Logging out " + currentUser.getName() + "...");
                    currentUser = null;
                    loggedIn = false;
                    FileUtils.saveAtLogout(Library.getInstance().bookCollection);
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

    private void bookSearch() {
        Library.getInstance().addStartBooks();
        System.out.println("Do you wanna search for\n1: Book title\n2: Author");
        Scanner scanner = new Scanner(System.in);
        int userInput = scanner.nextInt();
        switch (userInput) {
            case 1 -> Library.getInstance().searchBookTitle();
            case 2 -> Library.getInstance().searchBookAuthor();
            default -> System.out.println("Error! Unknown input");
        }
    }

    private void bookList() {
        Library.getInstance().addStartBooks();
        System.out.println("Please choose how you want to sort the list\n" +
                "1. Sort by title\n" +
                "2. Sort by author");
        Scanner scanner = new Scanner(System.in);
        int userInput = scanner.nextInt();
        switch (userInput) {
            case 1 -> Library.getInstance().displayBooksByTitle();
            case 2 -> Library.getInstance().displayBooksByAuthor();
            default -> System.out.println("Error! unknown input");
        }
    }
}