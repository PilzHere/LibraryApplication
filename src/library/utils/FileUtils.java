package library.utils;

import library.books.Book;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    //to save hashmap of books
    //to save list/hashmap of books that unavailable by which Lender

    //Method to check if file exist at start of program


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
