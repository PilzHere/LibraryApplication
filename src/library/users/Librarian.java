package library.users;

/**
 * A subclass of {@link User}.
 * Has more access than a {@link Lender}.
 */

public class Librarian extends User {

    /**
     * The librarian can access the {@link library.Library} as an administrator.
     *
     * @param name the user's name.
     */
    public Librarian (String name) {
        super(name);
    }
}