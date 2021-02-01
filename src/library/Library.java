package library;

import library.books.Book;
import library.users.Lender;
import library.users.User;
import library.utils.FileUtils;

//import java.text.AttributeEntry;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Library {
    private static final Library instance = new Library();
    public static final String ANSI_BLUE = "\u001B[34m";

    public static Library getInstance() {
        return instance;
    }

    public Library() {
        System.out.println("DEBUG: Library class instantiated. You should not see this message anymore.");
    }

    HashMap<String, Book> bookCollection = new HashMap<>();

    public void displayBookCollection() {
        System.out.println("The Library have the following books: ");

        bookCollection = FileUtils.readObjectFromFile("src/books.ser");

        List <Map.Entry<String, Book>> bookList = hashmapToList(bookCollection);
        bookList.forEach(book -> System.out.println(book.getValue()));
    }

    //admin to remove book from bookCollection
    public void removeBook() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter title of the book you wish to remove: ");
        String adminInput = scan.nextLine();

        if (validateStringInput(adminInput)) {
            Map.Entry<String, Book> foundBook= bookCollection.entrySet().stream()
                    .filter(book -> book.getValue().getTitle().equalsIgnoreCase(adminInput))
                    .findAny().orElse(null);

            if(foundBook != null){
                bookCollection.remove(foundBook);
                System.out.println(adminInput + " was deleted from book collection.");
            }else{
                System.out.println("No book with title " + adminInput + " was found.");
            }
            //TODO back to meny

        } else {
            System.out.println("Not a valid input");
            //Back to meny here
        }
    }

    //librarian - Add book
    public boolean addBook() {
        Scanner input = new Scanner(System.in);
        System.out.println(ANSI_BLUE+ "Enter book title: ");
        String bookTitle = input.nextLine();

        System.out.println(ANSI_BLUE+"Enter author: ");
        String author = input.nextLine();

        System.out.println(ANSI_BLUE+"Enter genre: ");
        String genre = input.nextLine();

        if (validateStringInput(bookTitle, author, genre)) {
            bookCollection.put(bookTitle, new Book(bookTitle, author, genre, true, ""));
            System.out.println(ANSI_BLUE+"Book added!");
            return true; // TODO use this value to return to meny
        } else {
            System.out.println(ANSI_BLUE+"Your input was not valid");
            return false; // TODO use this value to return to meny
        }
    }

    //Validation method to check string input
    public boolean validateStringInput(String... inputs) { //... = uncertain amount of inputs
        boolean valid = true;
        Pattern p = Pattern.compile("[a-zA-Z0-9\\-\\s\n]");
        //loop through inparameter inputs array
        for (String input : inputs) {
            Matcher m = p.matcher(input);
            if (!m.find()) { // regex to check a-z, 0-9 and -
                valid = false;
            }
        }
        return valid;
    }

    //prevent DRY. Takes bookCollection and returns a List-Map.Entry
    public List <Map.Entry<String, Book>> hashmapToList (HashMap<String, Book> bookCollection){
        List <Map.Entry<String, Book>> bookList = bookCollection.entrySet()
                .stream()
                .collect(Collectors.toList());

        return bookList;
    }

    //User - See available books
    public void checkAvailableBooks() {
        System.out.println("Available books to lend:");

        for (Map.Entry<String, Book> entry : bookCollection.entrySet()) {
            if (entry.getValue().isAvailable()) {
                System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor() + " | Genres: " + entry.getValue().getGenres());
            }
        }
    }

    public void lentBook(User user) {
        checkAvailableBooks();
        System.out.println(ANSI_BLUE + "Witch one would you like to rent?\nPlease enter Title or Author:");
        Scanner input = new Scanner(System.in);
        String bookToLent = input.nextLine();
        if (validateStringInput(bookToLent) && bookToLent.length() > 1) {

            Map.Entry<String, Book> book =
                    bookCollection.entrySet().stream()
                            .filter(b -> b.getValue().getTitle().equalsIgnoreCase(bookToLent) ||
                                    b.getValue().getAuthor().equalsIgnoreCase(bookToLent)).findAny().orElse(null);
            if (book != null) {
                System.out.println(ANSI_BLUE + "Borrowed - Title: " + book.getValue().getTitle() + " | Author: " + book.getValue().getAuthor() +
                        "\nDon't forget to return book within 2 weeks");
                book.getValue().setReservedBy(user.getName()); //sätt ReservedBy till låntagarens namn
                book.getValue().setAvailable(false);
                ((Lender)user).uppdateLendedBooks(book.getValue().getTitle());

            } else {
                System.out.println(ANSI_BLUE + "No such book was found!");
            }
        } else {
            System.out.println(ANSI_BLUE + "Your input was not valid");
        }
    }


    //librarian - check laoned books
    public void checkLoanedBooks() {
        System.out.println("Following book/books is lent out at the moment:");

        for (Map.Entry<String, Book> entry : bookCollection.entrySet()) {
            if (!entry.getValue().isAvailable()) {
                System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor() + " | Genres: " + entry.getValue().getGenres());
            }
        }
        //System.out.println(bookList.size());
    }

    // Search for a specific book by title
    public void searchBookTitle() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter search: ");
        String searchPhrase = input.nextLine();

        List<Map.Entry<String, Book>> bookList =
                bookCollection.entrySet().stream().filter(book -> book.getValue().getTitle().equals(searchPhrase))
                        .collect(Collectors.toList());

        for (Map.Entry<String, Book> book : bookList) {
            if (bookCollection.containsKey(searchPhrase)) {
                System.out.println("Book found!\n" + book.getValue());
            }
        }
    }

    //Admin to get list of Lenders
    public void getLenderList(List<User> users) {
        List<Lender> lenderList = new ArrayList<>();

        for (User user : users) {
            if (user instanceof Lender) {
                lenderList.add((Lender) user);
            }
        }
        System.out.println("Current Lenders: \n");
        lenderList.forEach(lender -> System.out.println(lender)); //Only prints names
    }


    //method to set a collection of 20-30 books.
    public void addStartBooks() {
        bookCollection.put("Sofies World",
                new Book("Sofies World", "Jostein Gaarder", "Philosophy", true, ""));
        bookCollection.put("Eileen",
                new Book("Eileen", "Ottessa Moshfegh", "Fiction", true, ""));
        bookCollection.put("Lord of the Rings: The Fellowship of the Rings",
                new Book("Lord of the Rings: The Fellowship of the Rings", "J.R.R Toliken", "Fantasy, Classic", true, ""));
        bookCollection.put("Lord of the Rings: The Two Towers",
                new Book("Lord of the Rings: The Two Towers", "J.R.R Toliken", "Fantasy, Classic", true, ""));
        bookCollection.put("Lord of the Rings: The Return of the King",
                new Book("Lord of the Rings: The Return of the King", "J.R.R Toliken", "Fantasy, Classic", true, ""));
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

        FileUtils.genericWrite(bookCollection, "src/books.ser");
    }

    public static void main(String[] args) {

        Library test = new Library();
        //test.addStartBooks();
        test.displayBookCollection();


    }



}