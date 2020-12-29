package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseManager {
    protected static Connection conn = null;
    protected static String connURL;
    protected static String connLogin;
    protected static String connPassword;	

    public static void setConnectionParameters(String url, String login, String password) throws SQLException {
    	disconnect();
    	connURL= url;
    	connLogin = login;
    	connPassword = password;
    	getConnection();
    }

    public static String getType(){
        return connURL.split(":")[1];
    }
    
    public static Connection getConnection() throws SQLException {
		if (conn == null || conn.isClosed()) {
	        conn = DriverManager.getConnection(connURL, connLogin, connPassword);
		}

        return conn;
    }
    
    public static void disconnect() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
 
}