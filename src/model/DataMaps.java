package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * SINGLETON OBJECT
 * This class creates relevant maps using data from the database.
 */
public class DataMaps {

    private Map<String, Employee> employees; //<key, name>
    private Map<String, String> workSteps; //<work id, work name>
    private Map<String, Float> productivityScores; //<work id, productivity score>
    private Map<String, String> nameKeyMap; //<name, key>
    private Map<String, LinkedList<Message>> preparedMessages;
    private Map<String, ArrayList<String>> preparedNotifications;
    private Map<String, ObservableList<TableRowData>> preparedWorkSteps;
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

    /**
     * This method is called on creation. It creates the maps using data from the database.
     */
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

                String key = rs2.getString("id");
                workSteps.put(key, rs2.getString("work_step_name"));

            }

            String sql3 = "select * from sql_factory.work_steps";
            ResultSet rs3 = statement.executeQuery(sql3);

            while (rs3.next()) {

                String key = rs3.getString("id");
                productivityScores.put(key, rs3.getFloat("productivity_score"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySqlDatabase.getInstance().disconnect(connection);
    }

    /**
     * This method returns employee names in a list.
     */
    public ObservableList<String> getWorkerNames() {

        ObservableList<String> workerNames = FXCollections.observableArrayList();
        for (Employee employee : employees.values()) {
            if (employee.getUserKey().substring(0, 1).equals("1"))
                workerNames.add(employee.getUserName());
        }
        return workerNames;
    }


    public void prepareNotificationsAndMessages(){

        preparedMessages = MySqlDatabase.getInstance().mapMessages();
        preparedNotifications = MySqlDatabase.getInstance().mapNotifications();

    }

    public void prepareWorkSteps(){

        preparedWorkSteps = MySqlDatabase.getInstance().mapWorkSteps();

    }

    public ObservableList<TableRowData> getWorkStepDataFromDate(String user_id, String date){

        ObservableList<TableRowData> todayData = FXCollections.observableArrayList();

        for(TableRowData row : getPreparedWorkSteps(user_id)){
            if(row.getDate().equals(date))
                todayData.add(row);
        }
        return todayData;
    }


    public Map<String, Employee> getEmployeeMap() { return employees; }

    public Map<String, String> getWorkStepsMap() { return workSteps; }

    Map<String, Float> getProductivityScoresMap() { return productivityScores; }

    public Map<String, String> getNameKeyMap() { return nameKeyMap; }

    public LinkedList<Message> getMessages(String user_id){ return preparedMessages.get(user_id); }

    public ObservableList<TableRowData> getPreparedWorkSteps(String user_id){ return  preparedWorkSteps.get(user_id); }

    public ArrayList<String> getNotifications(String user_id) { return preparedNotifications.get(user_id); }
}
