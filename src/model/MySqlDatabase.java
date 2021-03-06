package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import view.AlertBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

//SINGLETON OBJECT

public class MySqlDatabase {

    private static MySqlDatabase ourInstance;


    public static MySqlDatabase getInstance() {

        if (ourInstance == null)
            ourInstance = new MySqlDatabase();

        return ourInstance;

    }


    public void addWorkStep(TableRowData step) {

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Connection connection = connect();

                try {

                    Statement statement = connection.createStatement();

                    String sql = "insert into sql_factory.work_log" +
                            " values (" + "'" + step.getDate() + "'" + ", " + "'" + step.getTime() + "'" + ", " + "'" +
                            step.getWork_id() + "'" + ", " + "'" + step.getUser_id() + "'" + ", " + "'" + step.getAmount_done() + "'" +
                            ", " + "'" + step.getTrash_amount() + "'" + ", " + "'" + step.getReason() + "'" + ", " + "'" + step.getProductivity() + "'" +
                            ", " + "'" + step.getWork_step_name() + "'" + ")";

                    statement.executeUpdate(sql);

                } catch (SQLException e) {
                    e.printStackTrace();
                    new AlertBox("Problem with database.", 3);
                }
                disconnect(connection);
                return null;
            }
        };
        new Thread(task).start();
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
            disconnect(connection);
            return steps;

        } catch (SQLException e) {
            e.printStackTrace();
            new AlertBox("Problem with database.", 3);
            disconnect(connection);
            return null;
        }

    }


    public Map<String, ObservableList<TableRowData>> mapWorkSteps(){

        Connection connection = connect();
        Map<String, ObservableList<TableRowData>> steps = new HashMap<>();

        try {

            Statement statement = connection.createStatement();

            String sql = "select * from work_log order by _time";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                String key = rs.getString("employee_id");

                if(!steps.keySet().contains(key))
                    steps.put(key, FXCollections.observableArrayList());

                TableRowData row = new TableRowData(key);
                row.setDate(rs.getString("_date"));
                row.setTime(rs.getString("_time"));
                row.setWork_id(rs.getString("work_id"));
                row.setAmount_done(rs.getString("amount"));
                row.setTrash_amount(rs.getString("trash"));
                row.setReason(rs.getString("trash_reason"));
                row.setProductivity(rs.getString("productivity"));
                row.setWork_step_name(rs.getString("work_step_name"));

                steps.get(key).add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new AlertBox("Problem with database.", 3);
            disconnect(connection);
            return null;
        }
        disconnect(connection);
        return steps;
    }


    public void deleteWorkStep(TableRowData step){

        Connection connection = connect();

        try {

            Statement statement = connection.createStatement();

            String sql = "delete from sql_factory.work_log where employee_id = '" + step.getUser_id() + "' and _date = '" + step.getDate() + "' and _time = '" + step.getTime() + "' and work_id = '" + step.getWork_id() + "'";

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            new AlertBox("Problem with database.", 3);
        }
        disconnect(connection);
    }


    public void addNotification(String sender_id, String receiver_id) {

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Connection connection = connect();
                String timeAndDate = new TimeAndDateHelper().getTimeAndDate();

                try {

                    Statement statement = connection.createStatement();

                    String sql = "insert into sql_factory.notifications (employee_id, sender_id, time_date) select '" + receiver_id + "', '" + sender_id + "', '" + timeAndDate + "' where not exists (" +
                            "select 1 from sql_factory.notifications x where x.employee_id = '" + receiver_id + "' and x.sender_id = '" + sender_id + "')";

                    statement.executeUpdate(sql);

                } catch (SQLException e) {
                    e.printStackTrace();
                    new AlertBox("Problem with database.", 3);
                }
                disconnect(connection);
                return null;
            }
        };
        new Thread(task).start();

    }


    public ArrayList<String> getNotifications(String receiver_id) {

        Connection connection = connect();

        ArrayList<String> notifications = new ArrayList();

        try {

            Statement statement = connection.createStatement();

            String sql = "select * from sql_factory.notifications where employee_id = '" + receiver_id + "'";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                notifications.add(rs.getString("sender_id"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            new AlertBox("Problem with database.", 3);
            disconnect(connection);
            return null;
        }
        disconnect(connection);
        return notifications;
    }


    public void deleteNotifications(String user_id) {

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Connection connection = connect();

                try {

                    Statement statement = connection.createStatement();

                    String sql = "delete from sql_factory.notifications where employee_id = '" + user_id + "'";

                    statement.executeUpdate(sql);

                } catch (SQLException e) {
                    e.printStackTrace();
                    new AlertBox("Problem with database.", 3);
                }
                disconnect(connection);
                return null;
            }
        };
        new Thread(task).start();

    }


    public Map<String, ArrayList<String>> mapNotifications(){

        Connection connection = connect();

        Map<String, ArrayList<String>> notifications = new HashMap<>();

        try {

            Statement statement = connection.createStatement();

            String sql = "select * from sql_factory.notifications";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                String key = rs.getString("employee_id");

                if (!notifications.keySet().contains(key))
                    notifications.put(key, new ArrayList<>());

                notifications.get(key).add(rs.getString("sender_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new AlertBox("Problem with database.", 3);
            disconnect(connection);
            return null;
        }
        disconnect(connection);
        return notifications;

    }


    public void sendMessage(String sender_id, String receiver_id, String messeage, String timeStamp) {

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Connection connection = connect();

                try {

                    Statement statement = connection.createStatement();

                    String sql = "insert into sql_factory.messages values('" + receiver_id + "', '" + sender_id + "', '" + timeStamp + "', '" + messeage + "')";

                    statement.executeUpdate(sql);

                } catch (SQLException e) {
                    e.printStackTrace();
                    new AlertBox("Problem with database.", 3);
                }
                disconnect(connection);
                return null;
            }
        };
        new Thread(task).start();

    }

    public LinkedList<Message> getMessages(String receiver_id) {

        Connection connection = connect();

        LinkedList<Message> messages = new LinkedList<>();

        try {

            Statement statement = connection.createStatement();

            String sql = "select * from sql_factory.messages where employee_id = '" + receiver_id + "'";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                messages.addLast(new Message(rs.getString("sender_id"), rs.getString("employee_id"), rs.getString("content"), rs.getString("time_date")));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            new AlertBox("Problem with database.", 3);
            disconnect(connection);
            return null;
        }
        disconnect(connection);
        return messages;
    }


    public void deleteMessage(Message message) {

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                Connection connection = connect();

                try {

                    Statement statement = connection.createStatement();

                    String sql = "delete from sql_factory.messages where employee_id = '" + message.getReceiver() + "' and sender_id = '" + message.getSender() + "' and time_date = '" + message.getTimeStamp() + "'";

                    statement.executeUpdate(sql);

                } catch (SQLException e) {
                    e.printStackTrace();
                    new AlertBox("Problem with database.", 3);
                }
                disconnect(connection);
                return null;
            }
        };
        new Thread(task).start();
    }

    public Map<String, LinkedList<Message>> mapMessages(){
        Connection connection = connect();

        Map<String, LinkedList<Message>> messages = new HashMap<>();

        try {

            Statement statement = connection.createStatement();

            String sql = "select * from sql_factory.messages";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {

                String key = rs.getString("employee_id");

                if(!messages.keySet().contains(key))
                    messages.put(key, new LinkedList<>());

                messages.get(key).add(new Message(rs.getString("sender_id"), key, rs.getString("content"), rs.getString("time_date")));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            new AlertBox("Problem with database.", 3);
            disconnect(connection);
            return null;
        }
        disconnect(connection);
        return messages;
    }


    Connection connect() {
        final long startTime = System.currentTimeMillis();
        //String url = "jdbc:mysql://localhost/sql_factory?useUnicode=true&serverTimezone=UTC"; //Local host
        String url = "jdbc:mysql://factorymanager.cnkiejckzy7g.us-east-2.rds.amazonaws.com/sql_factory?useUnicode=true&serverTimezone=UTC"; //Aws server
        String user = "root";
        String password = "038913641249";

        try {

            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    void disconnect(Connection connection) {

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

