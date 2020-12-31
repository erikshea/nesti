package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class DatabaseManager {
    protected static Connection conn = null;
    protected static ObjectProperty<HashMap<String,String>> connectionParameters = new SimpleObjectProperty<>(new HashMap<>());
/*
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
		
		
    }*/
    /*
    public static void setConnectionParametersd(String type, String address, String name, String login, String password) throws SQLException {
    	Map<String,String> newConnectionParameters = new HashMap<>() ;
    	
    	disconnect();
    	connURL= "jdbc:" + type + (type.equals("sqlite")?":":"://")
				+ address.replaceFirst("/*$", ""); // remove trailing slashes from database address

    	connLogin = login;
    	connPassword = password;
    	
		if (getType().equals("mysql")) {
			var tempConnection = DriverManager.getConnection(connURL, connLogin, connPassword);
			tempConnection.createStatement().execute( "CREATE DATABASE IF NOT EXISTS " + name + ";" );
			tempConnection.close();
		}
		
		connURL += "/" + name;
		
    	if (type.equals("sqlite")) {
    		connURL += ".sqlite.db";
    	}
    	
    	newConnectionParameters.put("login", login);
    	newConnectionParameters.put("password", password);
    	newConnectionParameters.put("url", connURL);
    	
    	// try to connect so that exception is thrown if parameters incorrect.
    	getConnection();
    }*/

    public static void setConnectionParameters(String type, String address, String name, String login, String password) throws SQLException {

    	
    	disconnect();
    	var url= "jdbc:" + type + (type.equals("sqlite")?":":"://")
				+ address.replaceFirst("/*$", ""); // remove trailing slashes from database address


    	
		if (type.equals("mysql")) {
			var tempConnection = DriverManager.getConnection(url, login, password);
			tempConnection.createStatement().execute( "CREATE DATABASE IF NOT EXISTS " + name + ";" );
			tempConnection.close();
		}
		
		url += "/" + name;
		
    	if (type.equals("sqlite")) {
    		url += ".sqlite.db";
    	}

    	// Try to connect before setting new parameters.
    	DriverManager.getConnection(url, login, password).close();
    	
    	HashMap<String,String> newConnectionParameters = new HashMap<>() ;
    	newConnectionParameters.put("url", url);
    	newConnectionParameters.put("login", login);
    	newConnectionParameters.put("password", password);
    	
    	connectionParameters.set(newConnectionParameters);
    }
    
    
    
    public final static ObjectProperty<HashMap<String,String>> getConnectionParametersProperty(){
    	return connectionParameters;
    }
    
    public static String getType(){
        return connectionParameters.get().get("url").split(":")[1];
    }
    
    public static Connection getConnection() throws SQLException {
		if (conn == null || conn.isClosed()) {
			conn = DriverManager.getConnection(
				connectionParameters.get().get("url"),
				connectionParameters.get().get("login"),
				connectionParameters.get().get("password")
			);
		}

        return conn;
    }
    
    public static void disconnect() throws SQLException {
        if (conn != null && !conn.isClosed()) {
        	conn.close();
        }
    }

    
    
}