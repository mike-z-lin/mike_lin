package edu.uottawa.SEG2105.data.model;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    public static List<User>  users = null;
    public static String  currentUser;
    public static String  currentUserRole;
    public UserManager() {
        if (users == null)
            users = new ArrayList<User>();
    }

    public UserManager(String  c, String  cr) {
        currentUser = c;
        currentUserRole = cr;
        if (users == null)
            users = new ArrayList<User>();
    }
    public String getRole(){
        return currentUserRole;
    }
    public String getName(){
        return currentUser;
    }

    public String getRole(String name){
        for (User u : users) {
            if (u.name.equals(name)){
                return u.role;
            }
        }
        return "N.A.";
    }

    public boolean isAdmin(String n){
        for (User u : users) {
            if (u.name.equals(n) && u.role.equals("Admin")){
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin(){
        for (User u : users) {
            if (u.name.equals(currentUser) && u.role.equals("Admin")){
                return true;
            }
        }
        return false;
    }

    public boolean isLessor(){
        for (User u : users) {
            if (u.name.equals(currentUser) && u.role.equals("Lessor")){
                return true;
            }
        }
        return false;
    }
    public boolean isRenter(){
        for (User u : users) {
            if (u.name.equals(currentUser) && u.role.equals("Renter")){
                return true;
            }
        }
        return false;
    }
    public boolean hasAdmin() {
        for (User u : users) {
            if (u.role.equals("Admin")) {
                System.out.println("User name: " + u.email + ", role: " + u.role);
                return true;
            }
        }
        return false;
    }

    public boolean isCorrect(String n,String p){
        for (User u : users) {
            if (u.name.equals(n) && u.password.equals(p)){
                return true;
            }
        }
        return false;
    }

    public  String toString() {
        String re="";
        for (User u : users) {
            re += u.toString() + "\n";
        }
        return re;
    }
}
