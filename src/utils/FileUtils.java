package utils;

import library.Library;
import library.books.Book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class FileUtils {

    /**
     * Method to check if file exist at start of program
     *
     * @param hashmap books or lenders
     * @return hashmap with book objects or lender objects
     */
    public static HashMap checkIfFilesExists (HashMap hashmap) {
        File fileBooks = new File("src/books.ser");
        try {
            if (fileBooks.exists()) {
                hashmap = readObjectFromFileG(fileBooks);

            } else {
                Library.getInstance().addStartBooks();
                writeObjectToFileG(hashmap, fileBooks);
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }
        return hashmap;
    }

    /**
     * Generic method to write object to file
     *
     * @param hashmap
     * @param filename
     * @param <T>
     */
    public static <T> void writeObjectToFileG (HashMap<String, T> hashmap, File filename) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename, false)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(hashmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> HashMap<String, T> readObjectFromFileG (File filename) {
        HashMap<String, T> hashmap = null;

        try (FileInputStream fileInputStream = new FileInputStream(filename);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            hashmap = (HashMap<String, T>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashmap;
    }

    /**
     * metod to save any changes made to book collection
     *
     * @param hashmap
     */

    //TODO also for lender hashmap
    public static void saveAtLogout (HashMap<String, Book> hashmap) {
        File fileBooks = new File("src/books.ser");
        writeObjectToFileG(hashmap, fileBooks);
    }
}

//a