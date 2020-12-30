package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
 
import java.sql.ResultSet;
import java.sql.SQLException;


public class RegisteredUserDAO {
	private static final String TABLE_NAME = "registered_user";
	
    public static RegisteredUser find (String fieldName, String fieldValue) {
    	RegisteredUser result = null;
    	
    	try {
			var findUser = DatabaseManager.getConnection()
			    	.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE "+ fieldName +" = ?");

		    findUser.setString(1,  fieldValue);
			    
		    result = getFromResultSet(findUser.executeQuery());
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}

    	return result;
    }
 
    private static RegisteredUser getFromResultSet(ResultSet rs) throws SQLException
    {
    	RegisteredUser user = null;

        if (rs != null && !rs.isClosed() ) {
        	if ( DatabaseManager.getType().equals("sqlite") || rs.next() ) { // rs.next needs to be called before with mysql and checked against false
	        	user = new RegisteredUser();
	        	user.setUserId(rs.getInt("user_id"));
	        	user.setUsername(rs.getString("username"));
	        	user.setEmail(rs.getString("email"));
	        	user.setFirstName(rs.getString("first_name"));
	        	user.setLastName(rs.getString("last_name"));
	        	user.setCity(rs.getString("city"));
	        	user.setPasswordHash(rs.getString("password_hash"));
	        	user.setRegistrationDate(rs.getString("registration_date"));
        	}
        	if ( DatabaseManager.getType().equals("sqlite" ) ) {	// rs.next needs to be called after with sqlite
        		rs.next();
        	}
        }
        
        return user;
    }
 
    public static ObservableList<RegisteredUser> getAllRegisteredUsers () {
    	ObservableList<RegisteredUser> result = null;
    	
    	try {
        	var allUsersRs = DatabaseManager.getConnection().createStatement().executeQuery("SELECT * FROM " + TABLE_NAME + ";");
        	result = getAllFromResultSet( allUsersRs );;
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}

    	return result;
    }
 

    private static ObservableList<RegisteredUser> getAllFromResultSet(ResultSet rs) throws SQLException {
        ObservableList<RegisteredUser> userList = FXCollections.observableArrayList();
        var user = getFromResultSet(rs);
        
        while (user != null) {
    		userList.add(user);
    		user = getFromResultSet(rs);
        }
        
        return userList;
    }

    public static void update (RegisteredUser user) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME
			+ " SET username = ?,email = ?,first_name = ?,last_name = ?,city = ?,password_hash = ?,registration_date = ?"
			+ "WHERE user_id = ?;";

        var updateUser = DatabaseManager.getConnection().prepareStatement(sql);
        updateUser.setString(1,  user.getUsername());
        updateUser.setString(2,  user.getEmail());
        updateUser.setString(3,  user.getFirstName());
        updateUser.setString(4,  user.getLastName());
        updateUser.setString(5,  user.getCity());
        updateUser.setString(6,  user.getPasswordHash());
        updateUser.setString(7,  user.getRegistrationDate());
        updateUser.setInt(8,  user.getUserId());
        
        updateUser.executeUpdate();

    }
 
    public static void insert (RegisteredUser user) throws SQLException {
        var sql = "INSERT INTO "  + TABLE_NAME
			+ " (username, email, first_name, last_name, city, password_hash, registration_date)"
			+ "VALUES (?,?,?,?,?,?,?);";

        var insertUser = DatabaseManager.getConnection().prepareStatement(sql);
        insertUser.setString(1,  user.getUsername());
        insertUser.setString(2,  user.getEmail());
        insertUser.setString(3,  user.getFirstName());
        insertUser.setString(4,  user.getLastName());
        insertUser.setString(5,  user.getCity());
        insertUser.setString(6,  user.getPasswordHash());
        insertUser.setString(7,  user.getRegistrationDate());
        
        insertUser.executeUpdate();
  
        RegisteredUser insertedUser = find("username",user.getUsername());
        user.setUserId(insertedUser.getUserId());
    }
    
	public static void createTable() throws SQLException {
		DatabaseManager.getConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
        		+ "user_id INTEGER NOT NULL PRIMARY KEY "
        		+ ( DatabaseManager.getType().equals("mysql")?"AUTO_INCREMENT":"AUTOINCREMENT" )
        		+ ", username VARCHAR(200) NOT NULL UNIQUE"
        		+ ", email VARCHAR(200) NOT NULL UNIQUE"
        		+ ", first_name VARCHAR(200)"
        		+ ", last_name VARCHAR(200)"
        		+ ", city VARCHAR(200)"
        		+ ", password_hash VARCHAR(200) NOT NULL"
        		+ ", registration_date VARCHAR(200) );");
	}
	
	public static void populateTable() throws SQLException {
		resetTable();
		insert(new RegisteredUser("a", "a@a.a", "", "", "", "1aaaaaaaaaaaaaaaaaaaaaa"));
		insert(new RegisteredUser("judy", "dsq@d.dd", "", "", "", "1ddddddddddddddddddddddddd"));
		insert(new RegisteredUser("bob", "bobby@bob.bob", "Bobby", "Brown", "", "2bobb2ypassdddddddd"));
		insert(new RegisteredUser("jack", "jacky@d.dd", "", "", "", "dssqqsqq1ytyuytuyutytuyut"));
		insert(new RegisteredUser("stacy", "s@s.s", "", "", "", "ssssssssssssssssssssss1"));
		insert(new RegisteredUser("erik", "hoopsnale@gmail.com", "", "", "", "dssqqsqq1ytyuytuyutytuyut"));
	}
	
	public static void resetTable() throws SQLException {
    	DatabaseManager.getConnection().createStatement().execute("DELETE FROM " + TABLE_NAME + ";");
    }
}