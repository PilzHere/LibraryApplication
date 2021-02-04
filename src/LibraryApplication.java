import library.Library;
import library.Login;

public class LibraryApplication {
    public static void main(String[] args) { //Login login = new Login();
        Library l = new Library();
        Library.getInstance().addStartBooks();
        Library.getInstance().displayBooksByTitle();
    }
}