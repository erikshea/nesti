package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ApplicationSettings {
	private static Connection conn;
	
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
    
    public static Connection getConnection() throws SQLException {
		if (conn == null || conn.isClosed()) {
	        conn = DriverManager.getConnection("jdbc:sqlite:./assets/database/nesti_settings.sqlite.db","root","");
	        initialize();
		}
    	
        return conn;
    }
    
    public static String get(String key){
        var result = get("user_settings", key);
        
        return result == null? getDefault(key):result;
    }
    
    public static void set(String key, String value){
        set("user_settings", key, value);
    }
    
    public static String getDefault(String key){
        return get("default_settings", key);
    }
    
    public static void setDefault(String key, String value){
        set("default_settings", key, value);
    }
    
    public static String get(String tableName, String key){
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
