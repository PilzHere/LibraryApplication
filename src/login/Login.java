package login;

import library.Library;
import login.menuChoice.LoginMenuChoiceLender;
import login.menuChoice.LoginMenuChoiceLibrarian;
import login.users.Lender;
import login.users.Librarian;
import login.users.User;
import utils.FileUtils;

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
     * It will end when boolean isRunning is false. a
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

                // Sleep before getting user request again.
                try {
                    long napTime = 3L;
                    napTime *= 1000L; // to seconds.
                    Thread.sleep(napTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("\n\u001B[33mPlease come back soon!\u001B[0m");
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
        System.out.println("Please type your \u001B[32m" + "username" + "\u001B[0m to log in. Type " + "\u001B[32m" +
                "exit" + "\u001B[0m" + " to quit.");
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
                System.out.println("\nLogged in as \u001B[34m" + currentUser.getName() + "\u001B[0m.");
                return true;
            }
        }

        System.out.println("\nUser \u001B[31m" + userName + "\u001B[0m could not be found.");
        return false;
    }

    /**
     * Prints and listens for usable commands depending on {@link User} type:
     * {@link Librarian} or {@link Lender}.
     */
    private void getUserRequest () {
        Library.getInstance().bookCollection = FileUtils.checkIfFilesExists(Library.getInstance().bookCollection);
        Library.getInstance().remindToReturnBook(currentUser);

        if (currentUser instanceof Librarian)
            System.out.println("\n\u001B[33mWhat would you like to do? Pick an option.\u001B[0m\n1: Search for books.\n" +
                    "2: View all books.\n3: View list of lenders.\n4: Search lender and view lent books.\n" +
                    "5: Add or Remove lender.\n6: Add book.\n7: Remove book.\n8: View all borrowed books.\n" +
                    "9: View books by borrowed/return date.\n10: Log out user.");
        else
            System.out.println("\n\u001B[33mWhat would you like to do? Pick an option.\u001B[0m\n1: Search for books.\n" +
                    "2: Lend book.\n3: My lent books.\n4: View time left on lent book.\n" +
                    "5: View book by title/author.\n6: View all books.\n7: More detailed book info\n" +
                    "8: View available books.\n9: Return book.\n10: Log out user.");

        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            int userRequest = scanner.nextInt();

            if (currentUser instanceof Librarian) {
                final LoginMenuChoiceLibrarian choice = LoginMenuChoiceLibrarian.valueOf(userRequest);

                // Enum can be null if integer parameter is not an option.
                if (choice == null) {
                    printMessageNoOption();
                    return;
                }

                switch (choice) {
                    case SEARCH_FOR_BOOKS -> Library.getInstance().bookSearch();
                    case VIEW_ALL_BOOKS -> Library.getInstance().displayBookCollection();
                    case SEE_LIST_OF_LENDERS -> Library.getInstance().getLenderList(users);
                    case SEARCH_LENDER_AND_VIEW_LENDED_BOOKS -> Library.getInstance().searchForLender(users);
                    case ADD_REMOVE_LENDER -> Library.getInstance().addOrRemoveMenu(users);
                    case ADD_BOOK_TO_LIBARY -> Library.getInstance().addBook();
                    case REMOVE_BOOK_FROM_COLLECTION -> Library.getInstance().removeBook();
                    case SEE_BORROWED_BOOKS -> Library.getInstance().checkLoanedBooks();
                    case SEE_BORROWED_BOOKS_WITH_RETURN_DATES -> Library.getInstance().printBorrowedAndReturnDate();
                    case LOG_OUT_USER -> {
                        System.out.println("\nLogging out \u001B[34m" + currentUser.getName() + "\u001B[0m.\n");
                        FileUtils.saveAtLogout(Library.getInstance().bookCollection);
                        currentUser = null;
                        loggedIn = false;
                    }
                    default -> printMessageNoOption();
                }
            } else if (currentUser instanceof Lender) {
                final LoginMenuChoiceLender choice = LoginMenuChoiceLender.valueOf(userRequest);

                // Enum can be null if integer parameter is not an option.
                if (choice == null) {
                    printMessageNoOption();
                    return;
                }

                switch (choice) {
                    case SEARCH_FOR_BOOKS -> Library.getInstance().bookSearch();
                    case LEND_BOOKS -> Library.getInstance().lendBooks(currentUser);
                    case LIST_LENT_BOOKS -> Library.getInstance().booksBorrowed2(currentUser);
                    case SHOW_TIME_LEFT_ON_LENT_BOOK -> Library.getInstance().timeLeftOnBorrowedBooks(currentUser); //Library.getInstance().timeLeftOnLentBook();
                    case VIEW_BOOK_BY_TITLE_OR_AUTHOR -> Library.getInstance().bookList();
                    case VIEW_ALL_BOOKS -> Library.getInstance().displayBookCollection();
                    case MORE_DETAIL_BOOK -> Library.getInstance().moreInfoSpecificBookS(); //Library.getInstance().moreInfoSpecificBook();
                    case VIEW_AVAILABLE_BOOKS -> Library.getInstance().checkAvailableBooks();
                    case RETURN_BOOK -> Library.getInstance().returnBook();
                    case LOG_OUT_USER -> {
                        System.out.println("\nLogging out \u001B[34m" + currentUser.getName() + "\u001B[0m.\n");
                        FileUtils.saveAtLogout(Library.getInstance().bookCollection);
                        currentUser = null;
                        loggedIn = false;
                    }
                    default -> printMessageNoOption();
                }
            }
        } else {
            printMessageNoOption();
        }
    }

    private void printMessageNoOption () {
        System.out.println("\n\u001B[31mThat is not an option.\u001B[0m");
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