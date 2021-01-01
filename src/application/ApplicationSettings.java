package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Settings manager: static methods to get and set key-value pairs from an SQLite database file.
 *
 */
/**
 * @author hoops
 *
 */
public class ApplicationSettings {
	private static Connection conn; // connection object to retrieve settings
	
    /**
     * Creates a new SQLite file if it doesn't exist, store connection in static variable
     * @return connection object to SQLite settings file
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
		if (conn == null || conn.isClosed()) {
	        conn = DriverManager.getConnection("jdbc:sqlite:./assets/database/nesti_settings.sqlite.db","root","");
	        initialize();
		}
    	
        return conn;
    }
    

    /**
     * 	Create tables and set defaults
     * @throws SQLException if connection fails
     */
    private static void initialize() throws SQLException{
        conn.createStatement().execute(
    		"CREATE TABLE IF NOT EXISTS user_settings (key VARCHAR(200) NOT NULL PRIMARY KEY, value VARCHAR(200));"
        );
	        
        conn.createStatement().execute(
    		"CREATE TABLE IF NOT EXISTS default_settings (key VARCHAR(200) NOT NULL PRIMARY KEY, value VARCHAR(200));"
        );

        setDefault("databaseType"		, "sqlite");
        setDefault("databaseAddress"	, "./assets/database");
        setDefault("databaseName"		, "nesti");
        setDefault("databaseLogin"		, "root");
        setDefault("databasePassword"	, "");
    }
    
    /**
     * retrieve value in settings
     * @param key 
     * @return value corresponding to key. if doesn't exist, return default value (or null if it in turn doesn't exist)
     */
    public static String get(String key){
        var result = get("user_settings", key);
        
        return result == null? getDefault(key):result;
    }
    
    /**
     * Set a value in settings with a given key
     * @param key
     * @param value
     */
    public static void set(String key, String value){
        set("user_settings", key, value);
    }
    
    
    /**
     *  Get default value
     * @param key
     * @return
     */
    public static String getDefault(String key){
        return get("default_settings", key);
    }
    
    /**
     *  Set default value
     * @param key
     * @param value
     */
    public static void setDefault(String key, String value){
        set("default_settings", key, value);
    }
    
    /**
     *  Internal function to query a value from a table.
     * @param tableName table to query from
     * @param key column name in table
     * @return corresponding value
     */
    private static String get(String tableName, String key){
        String result = null;
        
        try {
			var getSetting = getConnection().prepareStatement(
				"SELECT value FROM " + tableName + " WHERE key = ?"
			);
			getSetting.setString(1, key);
			result = getSetting.executeQuery().getString("value");
		} catch (SQLException e) {}
    	
    	return result;
    }
    
    /**
     *  Internal function to set a value in a table.
     * @param tableName table to query from
     * @param key column name in table
     * @param corresponding value
     */
    private static void set(String tableName, String key, String value){
        try {
			var getSetting = getConnection().prepareStatement(
				"REPLACE INTO " + tableName + " (key, value) VALUES (?,?);"
			);
			getSetting.setString(1, key);
			getSetting.setString(2, value);
			getSetting.executeUpdate();
		} catch (SQLException e) {}
    }
}
