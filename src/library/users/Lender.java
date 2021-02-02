package library.users;

import java.util.ArrayList;
import java.util.List;

/**
 *  A subclass of {@link User}.
 *  Has less access than a {@link Librarian}.
 */

public class Lender extends User {

    /**
     * The lender borrows books from the {@link library.Library}.
     * @param name the user's name.
     */
    public Lender (String name) {
        super(name);
    }

    // List of books the user is currently lending
    List<String> lendedBooks = new ArrayList<>();

    public Lender (List<String> lendedBooks) {
        this.lendedBooks = lendedBooks;
    }

    public void uppdateLendedBooks(String title){
        this.lendedBooks.add(title);
    }

    public List<String> getLendedBooks () {
        return lendedBooks;
    }

    public void setLendedBooks (List<String> lendedBooks) {
        this.lendedBooks = lendedBooks;
    }


    @Override
    public String toString() {
        return "Lender{" +
                 lendedBooks +
                ", name='" + name + '\'' +
                '}';
    }
}