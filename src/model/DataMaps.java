package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//SINGLETON OBJECT

public class DataMaps {

    private Map<String, Employee> employees; //<Key, Name>
    private Map<Integer, String> workSteps;
    private static DataMaps ourInstance;

    public static DataMaps getInstance() {

        if(ourInstance == null){
            ourInstance = new DataMaps();
        }
        return ourInstance;
    }

    private DataMaps() { //Constructor
        map();
    }

    //Reads the employees and their keys from my database.
    private void map() {

        employees = new HashMap<>();
        workSteps = new HashMap<>();

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

            String sql2 = "select * from sql_factory.work_steps";
            ResultSet rs2 = statement.executeQuery(sql2);

            while (rs2.next()) {

                int key = rs2.getInt("id");
                workSteps.put(key, rs2.getString("work_step_name") + "," + rs2.getString("productivity_score"));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySqlDatabase.getInstance().disconnect(connection);
    }


    //Returns map.
    Map<String, Employee> getEmployeeMap() {
        return employees;
    }

    public Map<Integer, String> getWorkStepsMap(){
        return workSteps;
    }

    //Returns employee user names.
    public Collection<Employee> getEmployees() {
        return employees.values();
    }

}
