package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

//SINGLETON OBJECT

public class DataMaps {

    private Map<String, Employee> employees; //<Key, Name>
    private Map<Integer, String> workSteps;
    private Map<Integer, Float> productivityScores;
    private Map<String, String> nameKeyMap;
    private static DataMaps ourInstance;

    public static DataMaps getInstance() {

        if (ourInstance == null) {
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
        productivityScores = new HashMap<>();
        nameKeyMap = new HashMap<>();

        Connection connection = MySqlDatabase.getInstance().connect();
        try {
            Statement statement = connection.createStatement();
            String sql = "select * from sql_factory.employees";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                String key = rs.getString("id");
                String userName = new IOHelper().capitalizeFirsChar(rs.getString("user_name"));
                if (key.substring(0, 1).equals("1")) {
                    employees.put(key, new Worker(userName, key));
                    nameKeyMap.put(userName, key);
                } else {
                    employees.put(key, new Manager(userName, key));
                    nameKeyMap.put(userName, key);
                }
            }

            String sql2 = "select * from sql_factory.work_steps";
            ResultSet rs2 = statement.executeQuery(sql2);

            while (rs2.next()) {

                int key = rs2.getInt("id");
                workSteps.put(key, rs2.getString("work_step_name"));

            }

            String sql3 = "select * from sql_factory.work_steps";
            ResultSet rs3 = statement.executeQuery(sql3);

            while (rs3.next()) {

                int key = rs3.getInt("id");
                productivityScores.put(key, rs3.getFloat("productivity_score"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySqlDatabase.getInstance().disconnect(connection);
    }


    //Returns map.
    public Map<String, Employee> getEmployeeMap() {
        return employees;
    }

    public Map<Integer, String> getWorkStepsMap() {
        return workSteps;
    }

    public Map<Integer, Float> getProductivityScoresMap() {
        return productivityScores;
    }

    public Map<String, String> getNameKeyMap() {
        return nameKeyMap;
    }

    //Returns employee user names.
    public ObservableList<String> getWorkerNames() {

        ObservableList<String> workerNames = FXCollections.observableArrayList();
        for (Employee employee : employees.values()) {
            if (employee.getUserKey().substring(0, 1).equals("1"))
                workerNames.add(employee.getUserName());
        }
        return workerNames;
    }

}
