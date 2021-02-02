package library.utils;

import library.Library;
import library.books.Book;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    //to save hashmap of books
    //to save list/hashmap of books that unavailable by which Lender

    //Method to check if file exist at start of program
    public static HashMap checkIfFilesExists(HashMap hashmap) {
        File fileBooks = new File("src/books.ser");
        //TODO File fileUsers = new File("src/users.ser"); to save user and books that are on lend

        try {
            if (fileBooks.exists()) {  //&& fileUser.exists()
                System.out.println("there are saved objects in file. Call read method");
                hashmap = readObjectFromFileG(fileBooks);
            } else {
                System.out.println("no file exist. Call addBooks and writeToFile");
                hashmap = Library.getInstance().addStartBooks();
                writeObjectToFileG(hashmap, fileBooks);
            }

        }catch(NullPointerException e){
                e.getMessage();
        }
        return hashmap;
    }

    //TEST Generic
    public static <T> void writeObjectToFileG (HashMap<String, T>  hashmap, File filename){
        try(FileOutputStream fileOutputStream = new FileOutputStream(filename, false)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(hashmap);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static <T> HashMap<String, T> readObjectFromFileG(File filename){
        HashMap<String, T> hashmap =  null;

        try(FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){

            hashmap = (HashMap<String, T>) objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return hashmap;
    }

}
