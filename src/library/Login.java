package library;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Login {
    ArrayList<String> listOfUsers;
    ArrayList<String> listOfAdmins;

    public Login(){
        listOfUsers = new ArrayList<String>();
        listOfUsers.add("Amin");
        listOfUsers.add("Annika");
        listOfUsers.add("Sandra");
        listOfUsers.add("Christian");
        listOfUsers.add("Adam");
        listOfAdmins = new ArrayList<String>();
        listOfAdmins.add("Marcel");
        listOfAdmins.add("Johan");
    }
    /*
     * Returns the list of users
     */
    public ArrayList<String> users() {
        return listOfUsers;
    }

    /*
     * Returns the list of admins
     */
    public ArrayList<String> admins() {
        return listOfAdmins;
    }
    /*
     * Checks if a user is registered in the system. Returns true if it is.
     */
    public Boolean checkUser(String username){
        if(listOfUsers.contains(username) || listOfAdmins.contains(username)){
            return true;
        } else{
            return false;
        }
    }

    /*
     * Checks if current user is a regular user or an admin.
     */
    public void currentUser(String username) {
        if(this.checkUser(username) == true){
            if(listOfAdmins.contains(username)){
                System.out.println("Hi " + username + "!\n" +  "This user is an admin!");
            }else{
                System.out.print("Hi " + username + "!\n" +  "This user is a regular user!");
            }
        }else{
            System.out.println("This username is not in the system. Please try again!");
        }
    }

    //Just for testing
    /*
    public static void main(String args[]) {
        Login login = new Login();
        System.out.println(login.users());
        System.out.println(login.admins());
        login.checkUser("Adla");

    }*/

}