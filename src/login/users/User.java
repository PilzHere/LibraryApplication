package login.users;

/**
 * A user entity.
 */

public abstract class User {
    protected String name;

    public User () {
    }

    public User (String name) {
        this.name = name;
    }

    public String getName () {
        return name;
    }
}