package library.utils;

import library.books.Book;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    //to save hashmap of books
    //to save list/hashmap of books that unavailable by which Lender

    //Method to check if file exist at start of program
    public static void checkIfFilesExists(){

        try {
            File fileBooks = new File ("src/books.ser");
            File fileUser = new File ("src/users.ser");
            if (!fileBooks.exists() && !fileUser.exists()) {
                fileBooks.createNewFile();
                fileUser.createNewFile();
                System.out.println("File created");
            }
            else {
                readFromFile();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    }


    //needs to be generic to handle different objects?
    public static void writeObjectToFile (HashMap<String, Book> hashmap, String filename){

        try(FileOutputStream fileOutputStream = new FileOutputStream(filename, false)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(hashmap);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static HashMap<String, Book> readObjectFromFile(String filename){

        HashMap<String, Book> hashMap = null;

        try(FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)){

            hashMap = (HashMap<String, Book>) objectInputStream.readObject();

        }catch (Exception e){
            e.printStackTrace();
        }
        return hashMap;
    }


}
