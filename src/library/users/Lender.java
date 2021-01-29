package library.users;

import java.util.ArrayList;
import java.util.List;

public class Lender extends User {

    public Lender (String name) {
        super(name/*, false*/);
    }

    // List of books the user is currently lending
    List<String> lendedBooks; // = new ArrayList<>();

    public Lender (List<String> lendedBooks) {
        this.lendedBooks = lendedBooks;
    }

    public List<String> getLendedBooks () {
        return lendedBooks;
    }

    public void setLendedBooks (List<String> lendedBooks) {
        this.lendedBooks = lendedBooks;
    }
    // a


    @Override
    public String toString() {
        return "Lender{" +
                 lendedBooks +
                ", name='" + name + '\'' +
                '}';
    }
}