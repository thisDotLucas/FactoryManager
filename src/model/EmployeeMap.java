package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//SINGLETON OBJECT

public class EmployeeMap {

    private Map<String, Employee> employees; //<Key, Name>
    private static EmployeeMap ourInstance;

    public static EmployeeMap getInstance() {

        if(ourInstance == null){
            ourInstance = new EmployeeMap();
        }
        return ourInstance;
    }

    private EmployeeMap() { //Constructor
        map();
    }

    //Reads the employees and their keys from my database.
    private void map() {

        employees = new HashMap<>();
        Connection connection = MySqlDatabase.getInstance().connect();
        try {
            Statement statement = connection.createStatement();
            String sql = "select * from sql_factory.employees";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                String key = rs.getString("id");

                if(key.length() > 3)
                    employees.put(key , new Worker(rs.getString("user_name"), key));
                else
                    employees.put(key , new Manager(rs.getString("user_name"), key));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySqlDatabase.getInstance().disconnect(connection);
    }


    //Returns map.
    Map<String, Employee> getMap() {
        return employees;
    }

    //Returns employee user names.
    public Collection<Employee> getEmployees() {
        return employees.values();
    }

}
