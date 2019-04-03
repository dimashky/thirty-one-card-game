package rmi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    
    private Connection con;
    private final String url;
    private final String username;
    private final String password;
       
    public Database() {
        url = "jdbc:mysql://localhost:3333/history?autoReconnect=true&useSSL=false";
        username = "root";
        password = "root";  
    }
    
    public Connection getConnection() {
        //Loading driver
        try {
            Class.forName("com.mysql.jdbc");
           
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
           con = DriverManager.getConnection(url, username, password); 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return con;
    }
}
