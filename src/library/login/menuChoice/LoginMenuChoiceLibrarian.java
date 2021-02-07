package library.login.menuChoice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pilzhere
 * @created 05/02/2021 - 6:26 PM
 * @project LibraryApplication
 */
public enum LoginMenuChoiceLibrarian {
    SEARCH_FOR_BOOKS(1),
    VIEW_ALL_BOOKS(2),
    LIST_LENT_BOOKS(3),
    SEE_LIST_OF_LENDERS(4),
    SEARCH_LENDER_AND_VIEW_LENDED_BOOKS(5),
    SHOW_TIME_LEFT_ON_LENT_BOOK(6),
    ADD_BOOK_TO_LIBARY(7),
    REMOVE_BOOK_FROM_COLLECTION(8),
    SEE_BORROWED_BOOKS(9),
    SEE_BORROWED_BOOKS_WITH_RETURN_DATES(10),
    LOG_OUT_USER(11);

    private final int value;
    private static final Map map = new HashMap();

    LoginMenuChoiceLibrarian (int value) {
        this.value = value;
    }

    static {
        for (LoginMenuChoiceLibrarian choice : LoginMenuChoiceLibrarian.values()) {
            map.put(choice.value, choice);
        }
    }

    /**
     * Returns the enum based on int.
     *
     * @param value
     * @return value from key
     */
    public static LoginMenuChoiceLibrarian valueOf (int value) {
        return (LoginMenuChoiceLibrarian) map.get(value);
    }

    /**
     * Get int value from enum.
     *
     * @return int value from enum.
     */
    public int getValue () {
        return value;
    }
}
