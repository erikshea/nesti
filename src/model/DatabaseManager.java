package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import application.ApplicationSettings;

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
		if (getType().equals("mysql")) {
			DatabaseManager.getConnection().createStatement().execute(
				"CREATE DATABASE IF NOT EXISTS " + ApplicationSettings.get("databaseName") + ";"
			);
		}
    }
    
    public static void setConnectionParameters(String type, String address, String name, String login, String password) throws SQLException {
    	disconnect();
    	connURL= "jdbc:" + type + (type.equals("sqlite")?":":"://")
				+ address.replaceFirst("/*$", ""); // remove trailing slashes from database address

    	connLogin = login;
    	connPassword = password;
    	
		if (getType().equals("mysql")) {
			DatabaseManager.getConnection().createStatement().execute(
				"CREATE DATABASE IF NOT EXISTS " + ApplicationSettings.get("databaseName") + ";"
			);
			disconnect();
			
		}
		
		connURL += "/" + name;
		
    	if (type.equals("sqlite")) {
    		connURL += ".sqlite.db";
    	}
    	
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