package model;

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
