import library.Library;
import library.Login;

public class LibraryApplication {
    public static void main(String[] args) {
        Library lib = new Library();
        lib.addStartBooks();
        lib.searchBookTitle();
        //Login login = new Login();
    }
}