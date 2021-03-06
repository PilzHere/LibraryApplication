package login.menuChoice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pilzhere
 * @created 05/02/2021 - 6:42 PM
 * @project LibraryApplication
 */
public enum LoginMenuChoiceLender {
    SEARCH_FOR_BOOKS(1),
    LEND_BOOKS(2),
    LIST_LENT_BOOKS(3),
    SHOW_TIME_LEFT_ON_LENT_BOOK(4),
    VIEW_BOOK_BY_TITLE_OR_AUTHOR(5),
    VIEW_ALL_BOOKS(6),
    MORE_DETAIL_BOOK(7),
    VIEW_AVAILABLE_BOOKS(8),
    RETURN_BOOK(9),
    LOG_OUT_USER(10);

    private final int value;
    private static final Map map = new HashMap();

    LoginMenuChoiceLender (int value) {
        this.value = value;
    }

    static {
        for (LoginMenuChoiceLender choice : LoginMenuChoiceLender.values()) {
            map.put(choice.value, choice);
        }
    }

    /**
     * Returns the enum based on int.
     *
     * @param value
     * @return value from key
     */
    public static LoginMenuChoiceLender valueOf (int value) {
        return (LoginMenuChoiceLender) map.get(value);
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