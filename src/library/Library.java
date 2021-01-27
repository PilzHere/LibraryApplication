package library;

import library.books.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Library {

    HashMap <String, Book> bookCollection = new HashMap<>();


    public void displayBookCollection (){
        System.out.println("The Library have the following books: ");

        List<Map.Entry<String, Book>> bookList =
                bookCollection.entrySet().stream()
                        .collect(Collectors.toList());

        bookList.forEach(book -> System.out.println(book.getValue()));
        //change property available to a better printout, ex. available: yes/no
        //System.out.println(bookList.size());

    }

    //method to set a collection of 20-30 books.
    public void addStartBooks(){

        bookCollection.put("Sofies World", new Book("Sofies World", "Jostein Gaarder", "Philosophy", true));
        bookCollection.put("Eileen",
                new Book("Eileen", "Ottessa Moshfegh", "Fiction", true));
        bookCollection.put("Lord of the Rings: The Fellowship of the Rings",
                new Book("Lord of the Rings: The Fellowship of the Rings", "J.R.R Toliken","Fantasy, Classic", true ));
        bookCollection.put("Lord of the Rings: The Two Towers",
                new Book("Lord of the Rings: The Two Towers", "J.R.R Toliken","Fantasy, Classic", true ));
        bookCollection.put("Lord of the Rings: The Return of the King",
                new Book("Lord of the Rings: The Return of the King", "J.R.R Toliken","Fantasy, Classic", true ));
        bookCollection.put("Alice in Wonderland", new Book("Alice in Wonderland", "Lewis Carroll", "Classic", true));
        bookCollection.put("Crime and Punishment", new Book("Crime and Punishment", "Fjodor Dostojevskij", "Classic", true));
        bookCollection.put("Coraline", new Book("Coraline", "Neil Gaiman", "Fantasy", true));
        bookCollection.put("Siddhartha", new Book("Siddhartha", "Hermann Hesse", "Philosophy, Classic", true));
        bookCollection.put("Malmcolm X", new Book("Malmcolm X", "Manning Marable", "Biography", true));
        bookCollection.put("The Age of Bowie", new Book("The Age of Bowie", "Paul Morley", "Biography", true));
        bookCollection.put("Martin Luther King: a self-biograhpy", new Book("Martin Luther King: a self-biograhpy", "Martin Luther King", "Biography", true));
        bookCollection.put("No Logo", new Book("No Logo", "Naomi Klein", "Non-fiction", true));
        bookCollection.put("This Changes Everything", new Book("This Changes Everything", "Naomi Klein", "Non-fiction", true));
        bookCollection.put("I am Malala", new Book("I am Malala", "Malala Yousafzai", "Non-fiction, Biography", true));
        bookCollection.put("Carrie", new Book("Carrie", "Stephen King", "Horror", true));
        bookCollection.put("It", new Book("It", "Stephen King", "Horror", true));
        bookCollection.put("The Shining", new Book("The Shining", "Stephen King", "Horror", true));
        bookCollection.put("The Bell Jar", new Book("The Bell Jar", "Sylvia Plath", "Modern Classic, Fiction", true));
        bookCollection.put("The Sellout", new Book("The Sellout", "Paul Beatty", "Fiction", true));
        bookCollection.put("The Luminaries", new Book("The Luminaries", "Elenor Catton", "Fiction", true));
        bookCollection.put("The Plague", new Book("The Plague", "Albert Camus", "Modern Classic", true));
        bookCollection.put("Nocturner", new Book("Nocturner", "Kazuo Ishiguro", "Modern Classic", true));
    }

}
