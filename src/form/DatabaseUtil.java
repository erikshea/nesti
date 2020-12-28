package form;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {
    protected static Connection conn = null;
    protected static String JDBC_DRIVER;
    protected static String connStr;
    protected static String userName;
    protected static String password;	

    public static void setType(String databaseType) {
    	switch (databaseType) {
	    	default:
	            JDBC_DRIVER = "org.sqlite.JDBC";
	            connStr = "jdbc:sqlite:./assets/database/sqlite_users.db";
	            userName = "nestiroot";
	            password = "booyakasha456shinycat";
	    	break;	
    	}
    }


    public static void dbConnect() throws SQLException, ClassNotFoundException {
    	System.out.println(JDBC_DRIVER);
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC not found.");
            e.printStackTrace();
            throw e;
        }
        
        try {
            conn = DriverManager.getConnection(connStr, userName, password);
        } catch (SQLException e) {
            System.out.println("Connection Failed." + e);
            e.printStackTrace();
            throw e;
        }
    }
 
    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e){
           throw e;
        }
    }
 
    //DB Execute Query
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            dbConnect();
            System.out.println("Select statement: " + queryStmt + "\n");
 
            //Create statement
            stmt = conn.createStatement();
 
            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return resultSet
        return resultSet;
    }
 
    //DB Execute Update/Insert/Delete
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
    }
}