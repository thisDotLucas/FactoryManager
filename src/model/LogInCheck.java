package model;

import java.util.Map;


public class LogInCheck {

    Map<String, String> employees; //<Key, Name>
    String user;
    String key;


    public LogInCheck(String user, String key) {

        this.user = user;
        this.key = key;
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

        if(key.substring(0, 1).equals("1")){
            return 1;   //1 = worker
        } else {
            return 0;   //0 = manager
        }

    }

}