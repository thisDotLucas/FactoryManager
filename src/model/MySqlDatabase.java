package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import view.AlertBox;

import java.sql.*;

//SINGLETON OBJECT

public class MySqlDatabase {

    private static MySqlDatabase ourInstance;
    private String dataBase = "sql_factory";


    public static MySqlDatabase getInstance() {

        if (ourInstance == null)
            ourInstance = new MySqlDatabase();

        return ourInstance;

    }


    public void addWorkStep(TableRowData step){

        Connection connection = connect();

        try {

            Statement statement = connection.createStatement();

            String sql = "insert into sql_factory.work_log" +
                        " values (" + "'" + step.getDate() + "'" + ", " + "'" + step.getTime() + "'" + ", " +  "'" +
                        step.getWork_id() + "'" + ", " + "'" + step.getUser_id() + "'" + ", " + "'" + step.getAmount_done() + "'" +
                        ", " + "'" + step.getTrash_amount() + "'" + ", " + "'" + step.getReason() + "'" + ", " + "'" + step.getProductivity() + "'" +
                        ", " + "'" + step.getWork_step_name()+ "'" + ")";

            statement.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();
            new AlertBox("Problem with database.", 3);
        }
        disconnect(connection);
    }


    public ObservableList<TableRowData> getWorkSteps(String date, String user_id) {

        Connection connection = connect();
        ObservableList<TableRowData> steps = FXCollections.observableArrayList();
        try {

            Statement statement = connection.createStatement();

            String sql = "select * from work_log where _date = \"" + date + "\" and employee_id = \"" + user_id + "\" order by _time";
            ResultSet rs = statement.executeQuery(sql);
           
            while (rs.next()) {

                TableRowData row = new TableRowData(user_id);
                row.setDate(rs.getString("_date"));
                row.setTime(rs.getString("_time"));
                row.setWork_id(rs.getString("work_id"));
                row.setAmount_done(rs.getString("amount"));
                row.setTrash_amount(rs.getString("trash"));
                row.setReason(rs.getString("trash_reason"));
                row.setProductivity(rs.getString("productivity"));
                row.setWork_step_name(rs.getString("work_step_name"));

                steps.add(row);
            }

            return steps;

        } catch (SQLException e){
            e.printStackTrace();
            new AlertBox("Problem with database.", 3);
            return null;
        }

    }


    public Connection connect() {

        String url = "jdbc:mysql://localhost/sql_factory?useUnicode=true&serverTimezone=UTC";
        String user = "root";
        String password = "038913641249";

        try {
            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void disconnect(Connection connection){

        try {
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

}
//Statement statement = connection.createStatement();
//String sql = "select * from sql_factory.employees";
//ResultSet rs = statement.executeQuery(sql);

//while (rs.next()){
//  System.out.println(rs.getString("user_name"));
//}
