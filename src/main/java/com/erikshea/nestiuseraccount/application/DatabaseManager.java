package com.erikshea.nestiuseraccount.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Static methods provide a connection to a data source
 */
public class DatabaseManager {

    protected static Connection conn; // connection  object
    // Observable hashmap of parameters
    protected static ObjectProperty<HashMap<String, String>> connectionParameters = new SimpleObjectProperty<>(new HashMap<>());

    public static void setConnectionParameters(String type, String address, String name, String login, String password) throws SQLException {

        disconnect();
        var url = "jdbc:" + type + (type.equals("sqlite") ? ":" : "://")
                + address.replaceFirst("/*$", ""); // remove trailing slashes from database address

        if (type.equals("mysql")) { // sqlite automatically creates databases, so only needed for mysql
            var tempConnection = DriverManager.getConnection(url, login, password);
            tempConnection.createStatement().execute("CREATE DATABASE IF NOT EXISTS " + name + ";");
            tempConnection.close();
        }

        if (address.length() > 0){
            url += "/";
        }
        
        url += name;
        
        if (type.equals("sqlite")) { // Add file extension to sqlite database file
            url += ".sqlite.db";
        }
        System.out.println(url);
        // Try to connect before setting new parameters.
        DriverManager.getConnection(url, login, password).close();
        // If no exception, save parameters. 
        HashMap<String, String> newConnectionParameters = new HashMap<>(); // put parameters in temp hashmap.
        newConnectionParameters.put("url", url);
        newConnectionParameters.put("login", login);
        newConnectionParameters.put("password", password);
        connectionParameters.set(newConnectionParameters); // change is sent to listeners
    }

    /**
     * getter for observable connection parameters property. other parts of the
     * application can monitor connection changes.
     *
     * @return observable hashmap property
     */
    public final static ObjectProperty<HashMap<String, String>> getConnectionParametersProperty() {
        return connectionParameters;
    }

    /**
     * @return data source type, eg mysql, sqlite
     */
    public static String getType() {
        return connectionParameters.get().get("url").split(":")[1];
    }

    /**
     * return connection, try to create it if null
     *
     * @return connection to data source
     * @throws SQLException if connection fails
     */
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

    /**
     * Disconnect data source
     *
     * @throws SQLException if fails
     */
    public static void disconnect() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
