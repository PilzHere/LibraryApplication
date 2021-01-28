package library;

import library.books.Book;
import library.utils.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Library {
    private static final Library instance = new Library();

    public static Library getInstance() {
        return instance;
    }

    public Library() {
        System.out.println("DEBUG: Library class instantiated. You should not see this message anymore.");
    }

    HashMap<String, Book> bookCollection = new HashMap<>();

    public void displayBookCollection (){
        System.out.println("The Library have the following books: ");

        bookCollection = FileUtils.readObjectFromFile("src/books.txt");

        List<Map.Entry<String, Book>> bookList =
                bookCollection.entrySet().stream()
                        .collect(Collectors.toList());

        bookList.forEach(book -> System.out.println(book.getValue()));
        //change property available to a better printout, ex. available: yes/no

    }

    //admin to remove book from bookCollection
    public void removeBook() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter title of the book you wish to remove: \n");

        String adminInput = scan.nextLine();

        //Call try/catch method here to check input

        List<Map.Entry<String, Book>> bookList =
                bookCollection.entrySet().stream()
                        .filter(book -> book.getValue().getTitle().equalsIgnoreCase(adminInput))
                        .collect(Collectors.toList());


        bookCollection.remove(bookList.get(0).getKey());
        System.out.println(adminInput + " was deleted from book collections");

    }

    //librarian - Add book
    public boolean addBook() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter book title: ");
        String bookTitle = input.nextLine();

        System.out.println("Enter author: ");
        String author = input.nextLine();


        System.out.println("Enter genre: ");
        String genre = input.nextLine();

        if (validateStringInput(bookTitle, author, genre)) {
            bookCollection.put(bookTitle, new Book(bookTitle, author, genre, true));
            System.out.println("Book added!");
            return true; // TODO use this value to return to meny
        } else {
            System.out.println("Your input was not valid");
            return false; // TODO use this value to return to meny
        }
    }

    //Validation method to check string input
    public boolean validateStringInput(String... inputs) { //... = uncertain amount of inputs
        boolean valid = true;
        //loop through inparameter inputs array
        for (String input : inputs) {
            if (!input.matches("[a-zA-Z0-9\\-]")) { // regex to check a-z, 0-9 and -
                valid = false;
            }
        }
        return valid;
    }

    //User - See available books
    public void checkAvailableBooks() {
        System.out.println("Available books to lend:");

        for (Map.Entry<String, Book> entry : bookCollection.entrySet()) {
            if (entry.getValue().isAvailable() == true) {
                System.out.println("Title: " + entry.getValue().getTitle() + " | Author: " + entry.getValue().getAuthor() +  " | Genres: " + entry.getValue().getGenres()+"\n");
                System.out.printf("Title: %s Author: %s Genres: %s\n" + entry.getValue().getTitle() + entry.getValue().getAuthor() + entry.getValue().getGenres());

            }
        }
        //System.out.println(bookList.size());
    }

    //method to set a collection of 20-30 books.
    public void addStartBooks() {

        bookCollection.put("Sofies World",
                new Book("Sofies World", "Jostein Gaarder", "Philosophy", true));
        bookCollection.put("Eileen",
                new Book("Eileen", "Ottessa Moshfegh", "Fiction", true));
        bookCollection.put("Lord of the Rings: The Fellowship of the Rings",
                new Book("Lord of the Rings: The Fellowship of the Rings", "J.R.R Toliken", "Fantasy, Classic", true));
        bookCollection.put("Lord of the Rings: The Two Towers",
                new Book("Lord of the Rings: The Two Towers", "J.R.R Toliken", "Fantasy, Classic", true));
        bookCollection.put("Lord of the Rings: The Return of the King",
                new Book("Lord of the Rings: The Return of the King", "J.R.R Toliken","Fantasy, Classic", true ));
        bookCollection.put("Alice in Wonderland",
                new Book("Alice in Wonderland", "Lewis Carroll", "Classic", true));
        bookCollection.put("Crime and Punishment",
                new Book("Crime and Punishment", "Fjodor Dostojevskij", "Classic", true));
        bookCollection.put("Coraline",
                new Book("Coraline", "Neil Gaiman", "Fantasy", true));
        bookCollection.put("Siddhartha",
                new Book("Siddhartha", "Hermann Hesse", "Philosophy, Classic", true));
        bookCollection.put("Malmcolm X",
                new Book("Malmcolm X", "Manning Marable", "Biography", true));
        bookCollection.put("The Age of Bowie",
                new Book("The Age of Bowie", "Paul Morley", "Biography", true));
        bookCollection.put("Martin Luther King: a self-biograhpy",
                new Book("Martin Luther King: a self-biograhpy", "Martin Luther King", "Biography", true));
        bookCollection.put("No Logo",
                new Book("No Logo", "Naomi Klein", "Non-fiction", true));
        bookCollection.put("This Changes Everything",
                new Book("This Changes Everything", "Naomi Klein", "Non-fiction", true));
        bookCollection.put("I am Malala",
                new Book("I am Malala", "Malala Yousafzai", "Non-fiction, Biography", true));
        bookCollection.put("Carrie",
                new Book("Carrie", "Stephen King", "Horror", true));
        bookCollection.put("It",
                new Book("It", "Stephen King", "Horror", true));
        bookCollection.put("The Shining",
                new Book("The Shining", "Stephen King", "Horror", true));
        bookCollection.put("The Bell Jar",
                new Book("The Bell Jar", "Sylvia Plath", "Modern Classic, Fiction", true));
        bookCollection.put("The Sellout",
                new Book("The Sellout", "Paul Beatty", "Fiction", true));
        bookCollection.put("The Luminaries",
                new Book("The Luminaries", "Elenor Catton", "Fiction", true));
        bookCollection.put("The Plague",
                new Book("The Plague", "Albert Camus", "Modern Classic", true));
        bookCollection.put("Nocturner",
                new Book("Nocturner", "Kazuo Ishiguro", "Modern Classic", true));
    }

//Add book after libarians choice
    public void addBook() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter book title: ");
        String bookTitle = input.nextLine();

        System.out.println("Enter author: ");
        String author = input.nextLine();

        System.out.println("Enter genre: ");
        String genre = input.nextLine();

        bookCollection.put(bookTitle, new Book(bookTitle,author,genre,true));
        System.out.println("Book added!");
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
            } else { // <--- Why does this not work??
                System.out.println("No book title matching your search was found.\n" + "Please search for the full title.");
            }
        }
    }
