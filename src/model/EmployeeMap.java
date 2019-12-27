package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EmployeeMap {

    private Map<String, String> employees; //<Key, Name>

    EmployeeMap() { //Constructor
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
                employees.put(rs.getString("id"), rs.getString("user_name"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        MySqlDatabase.getInstance().disconnect(connection);
    }


    //Returns map.
    Map<String, String> getMap() {
        return employees;
    }

    //Returns employee user names.
    public Collection<String> getEmployeeNames() {
        return employees.values();
    }

}
