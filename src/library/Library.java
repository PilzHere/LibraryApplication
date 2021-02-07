package library;

import library.books.Book;
import library.users.Lender;
import library.users.User;
import library.utils.FileUtils;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
//import java.text.AttributeEntry;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Calendar;


public class Library {
    private static final Library instance = new Library();

    public static Library getInstance() {
        return instance;
    }

    public Library() {
        System.out.println("DEBUG: Library class instantiated. You should not see this message anymore.");

    }

    public HashMap<String, Book> bookCollection = new HashMap<>();

    //ADMIN METHODS

    /**Admin to remove book from bookCollection
     * check input from admin, secondly checks if book exists, if true -> book is removed
     */
    public void removeBook() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter title of the book you wish to remove: ");
        String adminInput = scan.nextLine();

        if (validateStringInput(adminInput)) {
            Map.Entry<String, Book> foundBook = bookCollection.entrySet().stream()
                    .filter(book -> book.getValue().getTitle().equalsIgnoreCase(adminInput))
                    .findAny().orElse(null);

            if (foundBook != null) {
                bookCollection.remove(foundBook.getKey());
                FileUtils.writeObjectToFileG(bookCollection, new File("src/books.ser"));
                System.out.println(adminInput + " was deleted from book collection.");

            } else {
                System.out.println("No book with title " + adminInput + " was found.");
            }

        } else {
            System.out.println("No valid input");
        }
    }

    //Admin - Add book
    public boolean addBook() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter book title: ");
        String bookTitle = input.nextLine();

        System.out.println("Enter author: ");
        String author = input.nextLine();

        System.out.println("Enter genre: ");
        String genre = input.nextLine();

        if (validateStringInput(bookTitle, author, genre)) {
            bookCollection.put(bookTitle, new Book(bookTitle, author, genre, true, ""));
            FileUtils.writeObjectToFileG(bookCollection, new File("src/books.ser"));
            System.out.println("Book added!");
            return true;
        } else {
            System.out.println("Your input was not valid");
            return false;
        }

    }

    //Admin to get list of Lenders
    public List<Lender> getLenderList(List<User> users) {
        List<Lender> lenderList = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Lender) {
                lenderList.add((Lender) user);
            }
        }
        System.out.println("Current Lenders: \n");
        lenderList.forEach(lender -> System.out.println(lender.getName() + " \n")); //Only prints names

        return lenderList;
    }

    //Admin to search for a Lender and view Lenders books
    public void searchForLender(List<User> users) {
        Scanner scan = new Scanner(System.in);
        List<Lender> lenderList = getLenderList(users);

        System.out.println("Enter name of Lender you wish to view: ");
        final String name = scan.next();

        if (validateStringInput(name)) {

        //TODO second validate, check if user is in userslist
        //filter out all books reserved by one lender
        List <Map.Entry<String, Book>> booksOnLend = bookCollection.entrySet().stream()
                .filter(book -> book.getValue().getReservedBy().equalsIgnoreCase(name))
                .collect(Collectors.toList());

            if(booksOnLend.size() != 0){
                System.out.println(name + " have lent: ");
                booksOnLend.stream().forEach(lender -> System.out.println(lender.getValue().getTitle()));
            }
            else{
                System.out.println(name + " has not lent any books.\n");
            }
        } else {
            System.out.println("Not a valid input.");
        }
    }

    //Admin - get list of borrowed books
    public HashMap<String, Book> getBorrowedBooks() {
        return (HashMap<String, Book>) bookCollection.entrySet().stream().filter(b -> !b.getValue().isAvailable()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //Admin - check borrowed and return date
    public void printBorrowedAndReturnDate() {
        HashMap<String, Book> borrowedBookList = getBorrowedBooks();
        for (Map.Entry<String, Book> entry : borrowedBookList.entrySet()) {
            LocalDate returnDate = entry.getValue().getBorrowedDate().plusDays(14);
            System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor() + " | Borrowed Date: " + entry.getValue().getBorrowedDate() + " | Return Date: " + returnDate);
        }
    }

    /*//prevent DRY. Takes bookCollection and returns a List-Map.Entry
    public List<Map.Entry<String, Book>> hashmapToList(HashMap<String, Book> bookCollection) {
        List<Map.Entry<String, Book>> bookList = bookCollection.entrySet()
                .stream()
                .collect(Collectors.toList());

        return bookList;
    }*/

    //LENDER METHODS

    //Lender - See available books
    public void checkAvailableBooks() {

        System.out.println("Available books to lend:");

        for (Map.Entry<String, Book> entry : bookCollection.entrySet()) {
            if (entry.getValue().isAvailable()) {
                System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor() + " | Genres: " + entry.getValue().getGenres());
            }
        }
    }

    //Lender - reminder to return book (SHOWS WHEN LENDERLOGS IN)
    public void remindToReturnBook(User user) {
        HashMap<String, Book> borrowedBooks = getBorrowedBooks();

        for (Map.Entry<String, Book> entry : borrowedBooks.entrySet()) {
            if (user.getName().equalsIgnoreCase(entry.getValue().getReservedBy())) {
                LocalDate returnDate = entry.getValue().getBorrowedDate().plusDays(14);
                LocalDate currentDate = LocalDate.now();

                if (returnDate.isEqual(currentDate) || returnDate.isBefore(currentDate)) {
                    System.out.println("\u001B[31m*** THE LEND PERIOD HAS EXPIRES FOR FOLLOWING BOOK/BOOKS ***\nTitle: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor()+"\u001B[O");
                }
            }
        }
    }

   /* //user - se my lended books
    public void booksBorrowed(User user) {
        //addStartBooks();

        if (((Lender) user).getLendedBooks().isEmpty()) {
            System.out.println("You have no borrowed book/books\n");
        } else {
            System.out.println("Your borrowed books: \n");
            ((Lender) user).getLendedBooks().forEach(System.out::println);
            System.out.println();
        }
    }*/

    //User to view lent books
    public void booksBorrowed2(User user) {

        List <Map.Entry<String, Book>> foundMatch = bookCollection.entrySet()
                .stream()
                .filter(book ->book.getValue().getReservedBy().equalsIgnoreCase(user.getName()))
                .collect(Collectors.toList());

        if(foundMatch.size() != 0){
            System.out.println(user.getName() + " have borrowed: ");
            foundMatch.stream().forEach(book-> System.out.println(book.getValue().getTitle()));
        }
        else{
            System.out.println("No borrowed book/books");
        }
    }

    //user - lend books
    public void lendBooks(User user) {
        checkAvailableBooks();
        System.out.println("Witch one would you like to rent?\nPlease enter Title or Author:");
        Scanner input = new Scanner(System.in);
        String bookToLent = input.nextLine();
        if (validateStringInput(bookToLent) && bookToLent.length() > 1) {

            Map.Entry<String, Book> book =
                    bookCollection.entrySet().stream()
                            .filter(b -> b.getValue().getTitle().equalsIgnoreCase(bookToLent) ||
                                    b.getValue().getAuthor().equalsIgnoreCase(bookToLent)).findAny().orElse(null);
            if (book != null) {
                System.out.println("Borrowed - Title: " + book.getValue().getTitle() + " | Author: " + book.getValue().getAuthor() +
                        "\nDon't forget to return book within 2 weeks");
                book.getValue().setReservedBy(user.getName()); //set ReservedBy to lendersName //TODO clear when returned
                book.getValue().setAvailable(false); // TODO clear when returned
                book.getValue().setBorrowedDate(LocalDate.now()); // TODO clear when returned

                FileUtils.writeObjectToFileG(bookCollection, new File ("src/books.ser"));

            } else {
                System.out.println("No such book was found!");
            }
        } else {
            System.out.println("Your input was not valid");
        }
    }

    //mer info om önskad bok
    public void moreInfoSpecificBook() {
        System.out.println("Please enter the book title you would like more information on: ");
        Scanner input = new Scanner(System.in);
        String bookOrAuthor = input.nextLine();

        if (bookCollection.containsKey(bookOrAuthor)) {
            System.out.println(bookCollection.get(bookOrAuthor).toString());
        } else {
            System.out.println("The book title you are looking for cannot be found :(");
        }
        //search for author
        for (String key : bookCollection.keySet()) {
            if (bookOrAuthor.equals(bookCollection.get(key).getAuthor())) {
                System.out.println(bookCollection.get(key).toString());
            } else {
                System.out.println("The author you are looking for cannot be found :(");
            }
        }
     /*   input.close();*/
    }

    //librarian AND lender - check laoned books
    public void checkLoanedBooks() {
        System.out.println("Following book/books is lent out at the moment:");
        for (Map.Entry<String, Book> entry : bookCollection.entrySet()) {
            if (!entry.getValue().isAvailable()) {
                System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor());
            }
        }
    }


    //METHODS FOR BOTH ADMIN AND LENDER

    // Search for a specific book by title
    public void searchBookTitle() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter search: ");
        String searchPhrase = input.nextLine();

        List<Map.Entry<String, Book>> bookList =
                bookCollection.entrySet().stream().filter(book -> book.getValue().getTitle().equals(searchPhrase)).collect(Collectors.toList());

        for (Map.Entry<String, Book> book : bookList) {
            if (book.getValue().getTitle().equalsIgnoreCase(searchPhrase)) {
                System.out.println("Book found!\n" + book.getValue());
            }
        }
        System.out.println("Search completed");
    }

    // Search for book(s) by author
    public void searchBookAuthor() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter search: ");
        String searchPhrase = input.nextLine();

        List<Map.Entry<String, Book>> bookList =
                bookCollection.entrySet().stream().filter(book -> book.getValue().getAuthor().equals(searchPhrase)).collect(Collectors.toList());

        for (Map.Entry<String, Book> book : bookList) {
            if (book.getValue().getAuthor().equalsIgnoreCase(searchPhrase)) {
                System.out.println("Author found!\n" + book.getValue());
            }
        }
        System.out.println("Search completed");


    }

    //Validation method to check string input
    public boolean validateStringInput(String... inputs) { //... = uncertain amount of inputs
        boolean valid = true;
        Pattern p = Pattern.compile("[a-zA-Z0-9\\-\\s\n]");
        //loop through inparameter inputs array
        for (String input : inputs) {
            Matcher m = p.matcher(input);
            if (!m.find()) { // regex to check a-z, 0-9 and -, whitespaces, newline
                valid = false;
            }
        }
        return valid;
    }

    public void displayBooksByTitle () {
        List<Map.Entry<String, Book>> listByTitle = bookCollection.entrySet()
                .stream().collect(Collectors.toList());
        listByTitle.sort(Comparator.comparing(book -> (book.getValue().getTitle())));
        System.out.println(listByTitle);

        //bookList.forEach(book -> System.out.println("Title: " + book.getValue().getTitle() + "| Author: " + book.getValue().getAuthor() + "| Genre: " + book.getValue().getGenres() + "| Available: " + book.getValue().isAvailable()));
        //change property available to a better printout, ex. available: yes/no
    }


    public void displayBooksByAuthor() {
        List<Map.Entry<String, Book>> listByAuthor = bookCollection.entrySet()
                .stream().collect(Collectors.toList());
        listByAuthor.sort(Comparator.comparing(book -> (book.getValue().getAuthor())));
        System.out.println(listByAuthor);
    }

    public void displayBookCollection() {
        System.out.println("The Library have the following books: \n");
        this.bookCollection.entrySet().forEach(book ->
                System.out.println("Title: " + book.getValue().getTitle()
                        + "| Author: " + book.getValue().getAuthor()));

    }

    //method to set a collection of 20-30 books.
    public void addStartBooks() {
        bookCollection.put("Sofies World",
                new Book("Sofies World", "Jostein Gaarder", "Philosophy", true, ""));
        bookCollection.put("Eileen",
                new Book("Eileen", "Ottessa Moshfegh", "Fiction", true, ""));
        bookCollection.put("Lord of the Rings: The Fellowship of the Rings",
                new Book("Lord of the Rings: The Fellowship of the Rings", "J.R.R Tolkien", "Fantasy, Classic", true, ""));
        bookCollection.put("Lord of the Rings: The Two Towers",
                new Book("Lord of the Rings: The Two Towers", "J.R.R Tolkien", "Fantasy, Classic", true, ""));
        bookCollection.put("Lord of the Rings: The Return of the King",
                new Book("Lord of the Rings: The Return of the King", "J.R.R Tolkien", "Fantasy, Classic", true, ""));
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
                new Book("Martin Luther King: a self-biograhpy", "Martin Luther King", "Biography", true, ""));
        bookCollection.put("No Logo",
                new Book("No Logo", "Naomi Klein", "Non-fiction", true, ""));
        bookCollection.put("This Changes Everything",
                new Book("This Changes Everything", "Naomi Klein", "Non-fiction", true, ""));
        bookCollection.put("I am Malala",
                new Book("I am Malala", "Malala Yousafzai", "Non-fiction, Biography", true, ""));
        bookCollection.put("Carrie",
                new Book("Carrie", "Stephen King", "Horror", true, ""));
        bookCollection.put("It",
                new Book("It", "Stephen King", "Horror", true, ""));
        bookCollection.put("The Shining",
                new Book("The Shining", "Stephen King", "Horror", true, ""));
        bookCollection.put("The Bell Jar",
                new Book("The Bell Jar", "Sylvia Plath", "Modern Classic, Fiction", true, ""));
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
    public void seeAllBooksInLibrary() {
        this.bookCollection.forEach((key, value) -> System.out.println("Title: " + value.getTitle() + " | Author: " + value.getAuthor()));
    }*/
}
