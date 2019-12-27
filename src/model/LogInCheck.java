package model;

import java.util.Map;


public class LogInCheck {

    Map<String, String> employees; //<Key, Name>
    String user;
    String key;


    public LogInCheck(String user, String key) {

        this.user = user;
        this.key = formatKey(key);
        employees = new EmployeeMap().getMap();

    }



    //We check that the key and user inputted matches.
    public boolean isEmployee() {

        if(employees.get(key) == null){
            return false;
        } else if(employees.get(key).equals(user.toLowerCase())){
            return true;
        } else {
            return false;
        }

    }


    //We check if the employee has worker or manager status.
    public int status() {

        if(key.length() == 4){
            return 1;   //1 = worker
        } else {
            return 0;   //0 = manager
        }

    }


    private String formatKey(String key) {
        try{
            return Integer.toString(Integer.parseInt(key));
        } catch (NumberFormatException e){
            return null;
        }
    }

}