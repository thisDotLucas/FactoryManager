package model;

import java.util.Map;


public class LogInCheck {

    private Map<String, Employee> employees; //<Key, Name>
    private String user;
    private String key;


    public LogInCheck(String user, String key) {

        this.user = user;
        this.key = key;
        employees = DataMaps.getInstance().getEmployeeMap();

    }



    //We check that the key and user inputted matches.
    public boolean isEmployee() {

        if(employees.get(key) == null){
            return false;
        } else if(employees.get(key).getUserName().equals(user.toLowerCase())){
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


}