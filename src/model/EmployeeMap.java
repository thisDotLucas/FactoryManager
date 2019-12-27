package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EmployeeMap {

    Map<String, String> employees; //<Key, Name>

    EmployeeMap(){
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


    Map<String, String> getMap(){
        return employees;
    }


    public Collection<String> getEmployeeNames(){
        return employees.values();
    }

}
