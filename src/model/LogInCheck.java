package model;

import java.util.Map;

/**
 * This class is used to check that the give user and key matches with each other and checks
 * the users access.
 */
public class LogInCheck {

    private Map<String, Employee> employees; //<Key, Name>
    private String user; //The user inputted user name.
    private String key; //The user inputted key.


    public LogInCheck(String user, String key) {

        this.user = user;
        this.key = key;
        employees = DataMaps.getInstance().getEmployeeMap();

    }

    /**
     * This method checks that the key and user strings matches.
     */
    public boolean isEmployee() {

        if (employees.get(key) == null) {
            return false;
        } else
            return employees.get(key).getUserName().toLowerCase().equals(user.toLowerCase());

    }


    /**
     * This method checks if the user has manager or worker access.
     */
    public int status() {

        if (key.substring(0, 1).equals("1")) {
            return 1;   //1 = worker
        } else {
            return 0;   //0 = manager
        }

    }

}