package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LogInCheck {

    Map<String, String> employees; //<Key, Name>
    String user;
    String key;


    public LogInCheck(String user, String key) {

        this.user = user;
        this.key = key;
        map();

    }


    //Reads the employee.txt file containing credentials and stores them in a map.
    private void map() {

        employees = new HashMap<>();
        String[] userAndKey;

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("employees.txt"));
            String line = reader.readLine();

            while (line != null) {

                userAndKey = line.split(","); //user and key are seperated with a comma each line.
                employees.put(userAndKey[1], userAndKey[0]);

                line = reader.readLine(); //next line

            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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