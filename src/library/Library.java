package library;

import library.books.Book;
import login.users.Lender;
import login.users.User;
import utils.FileUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class Library {
    private static final Library instance = new Library();
    public HashMap<String, Book> bookCollection = new HashMap<>();

    public static Library getInstance () {
        return instance;
    }

    /*public Library() {
        System.out.println("DEBUG: Library class instantiated. You should not see this message anymore.");
    }*/

    //******************LIBRARIAN METHODS*********************************************

    /**
     * Admin to remove book from bookCollection
     * check input from admin, secondly checks if book exists, if true -> book is removed
     */
    public void removeBook () {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nEnter \u001B[32mtitle\u001B[0m of the book you wish to remove: ");
        String adminInput = scan.nextLine();

        if (validateStringInput(adminInput)) {
            Map.Entry<String, Book> foundBook = bookCollection.entrySet().stream()
                    .filter(book -> book.getValue().getTitle().equalsIgnoreCase(adminInput))
                    .findAny().orElse(null);
            if (foundBook != null) {
                bookCollection.remove(foundBook.getKey());
                FileUtils.writeObjectToFileG(bookCollection, new File("src/books.ser"));
                System.out.println("\n\u001B[32m" + adminInput + "\u001B[0m was deleted from book collection.");

            } else {
                System.out.println("\n\u001B[31mNo book with title \u001B[32m" + adminInput + "\u001B[31m was found.\u001B[0m");
            }
        } else {
            printMessageErrorUnknownInput();
        }
    }

    //Admin - Add book
    public boolean addBook () {
        System.out.println("\nEnter book \u001B[32mtitle\u001B[0m: ");
        Scanner input = new Scanner(System.in);
        String bookTitle = input.nextLine();

        System.out.println("\nEnter \u001B[32mauthor\u001B[0m: ");
        String author = input.nextLine();

        System.out.println("\nEnter \u001B[32mgenre\u001B[0m: ");
        String genre = input.nextLine();

        if (validateStringInput(bookTitle, author, genre)) {
            bookCollection.put(bookTitle, new Book(bookTitle, author, genre, true, ""));
            FileUtils.writeObjectToFileG(bookCollection, new File("src/books.ser"));
            System.out.println("\n\u001B[32mBook added!\u001B[0m");
            return true;
        } else {
            printMessageErrorUnknownInput();
            return false;
        }
    }

    /**
     * @param users from Login - userList
     */
    //Admin to get list of Lenders
    public void getLenderList (List<User> users) {
        if (users != null) {
            System.out.println("\n\u001B[33mCurrent Lenders:\u001B[0m");

            users.forEach(user -> {
                if (user instanceof Lender) {
                    System.out.println(user.getName());
                }
            });
        }
    }

    //Admin to search for a Lender and view Lenders books
    public void searchForLender (List<User> users) {
        Scanner scan = new Scanner(System.in);
        getLenderList(users);
        System.out.println("\nEnter \u001B[32mname\u001B[0m of Lender you wish to view: ");
        final String name = scan.next();
        boolean foundMatch = false;

        for (User user : users) { //checks if name is in user-list
            if (user.getName().equalsIgnoreCase(name)) {
                foundMatch = true;
                break;
            }
        }
        if (foundMatch) {
            //filter out all books reserved by specific lender
            List<Map.Entry<String, Book>> booksOnLend = bookCollection.entrySet().stream()
                    .filter(book -> book.getValue().getReservedBy().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
            if (booksOnLend.size() != 0) {
                System.out.println("\n\u001B[34m" + name + " \u001B[0mhave borrowed: ");
                booksOnLend.stream().forEach(lender -> System.out.println(lender.getValue().getTitle()));
            } else {
                System.out.println("\n\u001B[34m" + name + " \u001B[0mhas not lent any books.\n");
            }
        } else {
            System.out.println("\n\u001B[31mNo lender with that name was found.\u001B[0m");
        }
    }

    //Admin - get list of borrowed books
    public HashMap<String, Book> getBorrowedBooks () {
        return (HashMap<String, Book>) bookCollection.entrySet().stream().filter(b -> !b.getValue().isAvailable())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //Admin - check borrowed and return date
    public void printBorrowedAndReturnDate () {
        HashMap<String, Book> borrowedBookList = getBorrowedBooks();
        System.out.println();
        for (Map.Entry<String, Book> entry : borrowedBookList.entrySet()) {
            LocalDate returnDate = entry.getValue().getBorrowedDate().plusDays(14);
            System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor() +
                    " | Borrowed Date: " + entry.getValue().getBorrowedDate() + " | Return Date: " + returnDate);
        }
    }

    public void addOrRemoveMenu (List<User> users) {
        System.out.println("\nDo you want to \u001B[32madd\u001B[0m or \u001B[32mremove\u001B[0m a lender \n1: Add lender \n" +
                "2: Remove lender");
        Scanner scanner = new Scanner(System.in);
        try {
            int adminInput = scanner.nextInt();
            switch (adminInput) {
                case 1 -> addLender(users);
                case 2 -> removeLender(users);
                default -> printMessageErrorUnknownInput(); // <- Deals with unexpected numbers
            }
        } catch (Exception e) {
            printMessageErrorUnknownInput(); // <- Deals with unexpected characters (anything that's not numbers)
        }
    }

    //Admin to remove user
    public void removeLender (List<User> users) {
        System.out.println("\nEnter \u001B[32mname of the user\u001B[0m you wish to remove:");
        getLenderList(users);
        Scanner scan = new Scanner(System.in);
        String adminInput = scan.nextLine();

        try {
            if (validateSingleStringInput(adminInput)) {
                List<User> lenders = users.stream().filter(user -> user instanceof Lender).collect(Collectors.toList());
                int nameFound = -1;

                for (User user : lenders) {
                    if (user.getName().equalsIgnoreCase(adminInput)) {
                        nameFound = users.indexOf(user);
                    }
                }
                if (nameFound > 0) {
                    users.remove(users.get(nameFound));
                    System.out.println("\n\u001B[34m" + adminInput + "\u001B[0m was removed.");
                } else {
                    System.out.println("\n\u001B[31mNo user with that name was found.\u001B[0m");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Admin add lender
    public void addLender (List<User> users) {
        System.out.println("\nEnter \u001B[32mname\u001B[0m of new lender:");
        Scanner scan = new Scanner(System.in);
        String newUserName = scan.nextLine();

        if (validateSingleStringInput(newUserName)) {
            users.add(new Lender(newUserName));
            System.out.println("\n\u001B[34m" + newUserName + "\u001B[0m was added as a Lender.");
        } else {
            printMessageErrorUnknownInput();
        }
    }

    //*****************LENDER METHODS**********************************************************

    //Lender - See available books
    public void checkAvailableBooks () {
        System.out.println("\n\u001B[33mAvailable books to lend:\u001B[0m");

        for (Map.Entry<String, Book> entry : bookCollection.entrySet()) {
            if (entry.getValue().isAvailable()) {
                System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor());
            }
        }
    }

    //Lender - reminder to return book (SHOWS WHEN LENDER LOGS IN)
    public void remindToReturnBook (User user) {
        HashMap<String, Book> borrowedBooks = getBorrowedBooks();

        for (Map.Entry<String, Book> entry : borrowedBooks.entrySet()) {
            if (user.getName().equalsIgnoreCase(entry.getValue().getReservedBy())) {
                LocalDate returnDate = entry.getValue().getBorrowedDate().plusDays(14);
                LocalDate currentDate = LocalDate.now();

                if (returnDate.isEqual(currentDate) || returnDate.isBefore(currentDate)) {
                    System.out.println("\n\u001B[31m*** The lending period has expired for the following book(s) ***\nTitle: " +
                            entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor() + "\u001B[O");
                }
            }
        }
    }

    //Lender - time left on rented book
    public void timeLeftOnLentBook (User user) {
        HashMap<String, Book> borrowedBookList = getBorrowedBooks();
        for (Map.Entry<String, Book> entry : borrowedBookList.entrySet()) {
            if (entry.getValue().getReservedBy().equalsIgnoreCase(user.getName())) {
                LocalDate returnDate = entry.getValue().getBorrowedDate().plusDays(14);
                LocalDate currentDate = LocalDate.now();
                long daysLeft = DAYS.between(currentDate, returnDate);
                System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor() +
                        " | Days left " + daysLeft);
            }
        }
    }

    //User to view lent books
    public void booksBorrowed2 (User user) {
        List<Map.Entry<String, Book>> foundMatch = bookCollection.entrySet()
                .stream()
                .filter(book -> book.getValue().getReservedBy().equalsIgnoreCase(user.getName()))
                .collect(Collectors.toList());

        if (foundMatch.size() != 0) {
            System.out.println("\n\u001B[33mYou have borrowed:\u001B[0m");
            foundMatch.forEach(book -> System.out.println(book.getValue().getTitle()));
        } else {
            System.out.println("\n\u001B[31mYou have no borrowed book.\u001B[0m");
        }
    }

    //user - lend books
    public void lendBooks (User user) {
        checkAvailableBooks();
        System.out.println("\n\u001B[33mWhich book would you like to borrow?\u001B[0m\nEnter \u001B[32mTitle\u001B[0m or \u001B[32mAuthor\u001B[0m:");
        Scanner input = new Scanner(System.in);
        String bookToLent = input.nextLine();
        if (validateStringInput(bookToLent) && bookToLent.length() > 1) {
            Map.Entry<String, Book> book =
                    bookCollection.entrySet().stream()
                            .filter(currentBook -> currentBook.getValue().getTitle().equalsIgnoreCase(bookToLent) ||
                                    currentBook.getValue().getAuthor().equalsIgnoreCase(bookToLent)).findAny().orElse(null);

            if (book != null) {
                System.out.println("\nBorrowed book - Title: " + book.getValue().getTitle() + " | Author: " +
                        book.getValue().getAuthor() + "\nDon't forget to return book within 2 weeks");
                book.getValue().setReservedBy(user.getName());
                book.getValue().setAvailable(false);
                book.getValue().setBorrowedDate(LocalDate.now());

                FileUtils.writeObjectToFileG(bookCollection, new File("src/books.ser"));
            } else {
                System.out.println("\n\u001B[31mNo such book was found.\u001B[0m");
            }
        } else {
            System.out.println("\n\u001B[31mYour input was not valid.\u001B[0m");
        }
    }

    //Time left of borrowed book
    public void timeLeftOnBorrowedBooks (User user) {
        for (Map.Entry<String,
                Book> entry : bookCollection.entrySet()) {
            if (user.getName().equalsIgnoreCase(entry.getValue().getReservedBy())) {
                LocalDate currentDate =
                        LocalDate.now();
                LocalDate returnDate = entry.getValue().getBorrowedDate().plusDays(14);
                Period period =
                        Period.between(currentDate, returnDate);
                System.out.println("Title: " +
                        entry.getValue().getTitle()
                        + " expires in " + period.getDays()
                        + " days");
            }
        }
    }

    // Lender - return borrowed book
    public void returnBook () {
        Scanner input = new Scanner(System.in);
        System.out.println("\nWhats the \u001B[32mtitle\u001B[0m on the book you like to return? ");
        String bookReturn = input.nextLine();

        if (validateStringInput(bookReturn) && bookReturn.length() > 1) {
            Map.Entry<String, Book> book = bookCollection.entrySet().stream()
                    .filter(b -> b.getKey().equalsIgnoreCase(bookReturn)).findAny().orElse(null);
            if (book != null) {
                System.out.println("\n\u001B[33mYou have returned\u001B[0m - Title: " + book.getValue().getTitle() + " | Author: " +
                        book.getValue().getAuthor());
                book.getValue().setReservedBy("");
                book.getValue().setAvailable(true);
                book.getValue().setBorrowedDate(null);

                FileUtils.writeObjectToFileG(bookCollection, new File("src/books.ser"));
            } else {
                System.out.println("\n\u001B[31mNo such book was found.\u001B[0m");
            }
        } else {
            System.out.println("\n\u001B[31mYour input was not valid.\u001B[0m");
        }
    }

    //CAN BE REMOVED IF THE OTHER ONE WORKS- SANDRA
    public void moreInfoSpecificBookS () {
        System.out.println("\nPlease enter the book \u001B[32mtitle\u001B[0m you would like more information on: ");
        Scanner input = new Scanner(System.in);
        String bookOrAuthor = input.nextLine();

        if (validateSingleStringInput(bookOrAuthor)) {
            Map.Entry<String, Book> foundBook =
                    bookCollection.entrySet().stream()
                            .filter(book -> book.getValue().getTitle().equalsIgnoreCase(bookOrAuthor))
                            .findAny().orElse(null);

            if (foundBook != null) {
                System.out.println("\n\u001B[33mThe Library found the following book:\u001B[0m\nTitle: " + foundBook.getValue()
                        .getTitle() + "\nAuthor: " + foundBook.getValue().getAuthor() + "\nGenre: " + foundBook
                        .getValue().getGenres() + "\nAvailable: " + foundBook.getValue().isAvailable());
            } else {
                System.out.println("\n\u001B[31mNo book with that title was found.\u001B[0m");
            }
        } else {
            printMessageErrorUnknownInput();
        }
    }

    //mer info om önskad bok
    public void moreInfoSpecificBook () {
        System.out.println("\nPlease enter the book \u001B[32mtitle\u001B[0m you would like more information on: ");
        Scanner input = new Scanner(System.in);
        String bookOrAuthor = input.nextLine();

        if (bookCollection.containsKey(bookOrAuthor)) {
            System.out.println(bookCollection.get(bookOrAuthor).toString());
        } else {
            System.out.println("\n\u001B[31mThe book title you are looking for cannot be found.\u001B[0m");
        }
        //search for author
        for (String key : bookCollection.keySet()) {
            if (bookOrAuthor.equals(bookCollection.get(key).getAuthor())) {
                System.out.println(bookCollection.get(key).toString());
            } else {
                System.out.println("\n\u001B[31mThe author you are looking for cannot be found.\u001B[0m");
            }
        }
        /*   input.close();*/
    }

    //librarian AND lender - check lent books
    public void checkLoanedBooks () {
        System.out.println("\n\u001B[33mFollowing book(s) is/are lent out at the moment:\u001B[0m");
        for (Map.Entry<String, Book> entry : bookCollection.entrySet()) {
            if (!entry.getValue().isAvailable()) {
                System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor());
            }
        }
    }

    //***************METHODS FOR BOTH ADMIN AND LENDER********************************************

    // Search for a specific book by title
    public void searchBookTitle () {
        System.out.println("\nEnter \u001B[32msearch words\u001B[0m seperated with space...");
        final Scanner input = new Scanner(System.in);
        final String[] words = input.nextLine().toLowerCase().split("\\s");
        ArrayList<Book> foundBooks = new ArrayList<>();

        // Add matching books to foundBooks list.
        for (String word : words) {
            for (Book book : bookCollection.values()) {
                if (book.getTitle().toLowerCase().contains(word)) {
                    foundBooks.add(book);
                }
            }
        }

        foundBooks = removeDuplicatedBooksFromList(foundBooks);
        printBooksFoundFromSearch(foundBooks);
    }

    // Search for book(s) by author
    public void searchBookAuthor () {
        System.out.println("\nEnter \u001B[32msearch words\u001B[0m seperated with space...");
        final Scanner input = new Scanner(System.in);
        final String[] words = input.nextLine().toLowerCase().split("\\s");
        ArrayList<Book> foundBooks = new ArrayList<>();

        // Add matching books to foundBooks list.
        for (String word : words) {
            for (Book book : bookCollection.values()) {
                if (book.getAuthor().toLowerCase().contains(word)) {
                    foundBooks.add(book);
                }
            }
        }

        foundBooks = removeDuplicatedBooksFromList(foundBooks);
        printBooksFoundFromSearch(foundBooks);
    }

    /**
     * Removes duplicated books from ArrayList.
     *
     * @param bookList
     * @return
     */
    private ArrayList<Book> removeDuplicatedBooksFromList (final ArrayList<Book> bookList) {
        for (int i = 0; i < bookList.size(); i++) {
            for (int j = i + 1; j < bookList.size(); j++) {
                if (bookList.get(i).getTitle().equalsIgnoreCase(bookList.get(j).getTitle())) {
                    bookList.remove(j);
                }
            }
        }

        return bookList;
    }

    /**
     * Prints books found in list if size greater than 0.
     *
     * @param bookList
     */
    private void printBooksFoundFromSearch (final ArrayList<Book> bookList) {
        if (bookList.size() == 0) {
            System.out.println("\n\u001B[31mNo books found matching your search criteria.\u001B[0m");
            return;
        }

        System.out.println("\n\u001B[33mThese books matches your search criteria:\u001B[0m");
        for (Book book : bookList) {
            System.out.println(book.getTitle());
        }
    }

    // List all books alphabetically sorted by title
    public void displayBooksByTitle () {
        List<Map.Entry<String, Book>> listByTitle = bookCollection.entrySet()
                .stream().collect(Collectors.toList());
        listByTitle.sort(Comparator.comparing(book -> (book.getValue().getTitle())));

        for (Map.Entry<String, Book> stringBookEntry : listByTitle) {
            System.out.println(
                    "Title: " + stringBookEntry.getValue().getTitle() +
                            " | Author: " + stringBookEntry.getValue().getAuthor());
        }
    }

    // List all books alphabetically sorted by author
    public void displayBooksByAuthor () {
        List<Map.Entry<String, Book>> listByAuthor = bookCollection.entrySet()
                .stream().collect(Collectors.toList());
        listByAuthor.sort(Comparator.comparing(book -> (book.getValue().getAuthor())));

        for (Map.Entry<String, Book> stringBookEntry : listByAuthor) {
            System.out.println(
                    "Author: " + stringBookEntry.getValue().getAuthor() +
                            " | Title: " + stringBookEntry.getValue().getTitle());
        }
    }

    public void displayBookCollection () {
        System.out.println("\n\u001B[33mThe Library have the following books:\u001B[0m");
        this.bookCollection.forEach((key, value) -> System.out.println("Title: " + value.getTitle()
                + " | Author: " + value.getAuthor()));
    }

    public void bookSearch () {
        System.out.println("\nPlease \u001B[32mchoose\u001B[0m what you would like to search for.\n" +
                "1: Book title\n" +
                "2: Author");
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextInt()) {
            final int userInput = scanner.nextInt();
            getChoiceBookSearchFunctions(userInput);

            return;
        }
        printMessageErrorUnknownInput(); // <- Deals with unexpected characters (anything that's not numbers)
    }

    public void bookList () {
        System.out.println("\nPlease \u001B[32mchoose\u001B[0m how you want to sort the list.\n" +
                "1. Sort by title\n" +
                "2. Sort by author");
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextInt()) {
            final int userInput = scanner.nextInt();
            getChoiceBookListFunctions(userInput);

            return;
        }
        printMessageErrorUnknownInput(); // <- Deals with unexpected characters (anything that's not numbers)
    }

    private void getChoiceBookListFunctions (final int choice) {
        switch (choice) {
            case 1 -> displayBooksByTitle();
            case 2 -> displayBooksByAuthor();
            default -> printMessageErrorUnknownInput(); // <- Deals with unexpected numbers
        }
    }

    private void getChoiceBookSearchFunctions (final int choice) {
        switch (choice) {
            case 1 -> searchBookTitle();
            case 2 -> searchBookAuthor();
            default -> printMessageErrorUnknownInput(); // <- Deals with unexpected numbers
        }
    }

    //***VALIDATION METHODS***
    //Validation method to check one or more string input
    public boolean validateStringInput (String... inputs) { //... = uncertain amount of inputs
        boolean valid = true;
        Pattern p = Pattern.compile("^[a-zA-Z0-9\\-\\s\n]+$");
        //loop through inparameter inputs array
        for (String input : inputs) {
            Matcher m = p.matcher(input);
            if (!m.find()) { // regex to check a-z, 0-9 and -, whitespaces, newline
                valid = false;
            }
        }
        return valid;
    }

    //Validation for 1 single string
    public boolean validateSingleStringInput (String input) {
        Pattern p = Pattern.compile("^[a-zA-Z]+$");
        Matcher m = p.matcher(input);

        return m.matches();
    }

    private void printMessageErrorUnknownInput () {
        System.out.println("\n\u001B[31mThat is not an option.\u001B[0m");
    }

    //DO NOT USE IN METHODS - ONLY FOR SAVE TO FILE
    public void addStartBooks () {
        bookCollection.put("Sofies World",
                new Book("Sofies World", "Jostein Gaarder", "Philosophy", true, ""));
        bookCollection.put("Eileen",
                new Book("Eileen", "Ottessa Moshfegh", "Fiction", true, ""));
        bookCollection.put("Lord of the Rings: The Fellowship of the Rings",
                new Book("Lord of the Rings: The Fellowship of the Rings", "J.R.R Tolkien", "Fantasy, Classic",
                        true, ""));
        bookCollection.put("Lord of the Rings: The Two Towers",
                new Book("Lord of the Rings: The Two Towers", "J.R.R Tolkien", "Fantasy, Classic",
                        true, ""));
        bookCollection.put("Lord of the Rings: The Return of the King",
                new Book("Lord of the Rings: The Return of the King", "J.R.R Tolkien", "Fantasy, Classic",
                        true, ""));
        bookCollection.put("Alice in Wonderland",
                new Book("Alice in Wonderland", "Lewis Carroll", "Classic", true, ""));
        bookCollection.put("Crime and Punishment",
                new Book("Crime and Punishment", "Fjodor Dostojevskij", "Classic", true, ""));
        bookCollection.put("Coraline",
                new Book("Coraline", "Neil Gaiman", "Fantasy", true, ""));
        bookCollection.put("Siddhartha",
                new Book("Siddhartha", "Hermann Hesse", "Philosophy, Classic", true, ""));
        bookCollection.put("Malmcolm X",
                new Book("Malmcolm X", "Manning Marable", "Biography", true, ""));
        bookCollection.put("The Age of Bowie",
                new Book("The Age of Bowie", "Paul Morley", "Biography", true, ""));
        bookCollection.put("Martin Luther King: a self-biograhpy",
                new Book("Martin Luther King: a self-biograhpy", "Martin Luther King", "Biography",
                        true, ""));
        bookCollection.put("No Logo",
                new Book("No Logo", "Naomi Klein", "Non-fiction", true, ""));
        bookCollection.put("This Changes Everything",
                new Book("This Changes Everything", "Naomi Klein", "Non-fiction", true,
                        ""));
        bookCollection.put("I am Malala",
                new Book("I am Malala", "Malala Yousafzai", "Non-fiction, Biography", true,
                        ""));
        bookCollection.put("Carrie",
                new Book("Carrie", "Stephen King", "Horror", true, ""));
        bookCollection.put("It",
                new Book("It", "Stephen King", "Horror", true, ""));
        bookCollection.put("The Shining",
                new Book("The Shining", "Stephen King", "Horror", true, ""));
        bookCollection.put("The Bell Jar",
                new Book("The Bell Jar", "Sylvia Plath", "Modern Classic, Fiction", true,
                        ""));
        bookCollection.put("The Sellout",
                new Book("The Sellout", "Paul Beatty", "Fiction", true, ""));
        bookCollection.put("The Luminaries",
                new Book("The Luminaries", "Elenor Catton", "Fiction", true, ""));
        bookCollection.put("The Plague",
                new Book("The Plague", "Albert Camus", "Modern Classic", true, ""));
        bookCollection.put("Nocturner",
                new Book("Nocturner", "Kazuo Ishiguro", "Modern Classic", true, ""));
    }

    /*//Metod to see ALL books avalible
    public void seeAllBooksInLibrary(){
        this.bookCollection.forEach((key, value) -> System.out.println("Title: " + value.getTitle() + " | Author: " +
        value.getAuthor() + " | Genres: " + value.getGenres()));
    }*/
}
