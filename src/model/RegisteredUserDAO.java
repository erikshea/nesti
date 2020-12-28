package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
 
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisteredUserDAO {
    public static RegisteredUser findRegisteredUser (String fieldName, String fieldValue) throws SQLException {
        var findUser = DatabaseManager.getConnection()
        	.prepareStatement("SELECT * FROM registered_users WHERE "+ fieldName +" = ?");
        findUser.setString(1,  fieldValue);
        
        return getRegisteredUserFromResultSet(findUser.executeQuery());
    }
 
    private static RegisteredUser getRegisteredUserFromResultSet(ResultSet rs) throws SQLException
    {
    	RegisteredUser user = null;
        if (rs != null) {
        	if ( !DatabaseManager.getType().equals("sqlite" ) ) {
        		rs.next();
        	}
        	user = new RegisteredUser();
        	user.setUserId(rs.getInt("user_id"));
        	user.setUsername(rs.getString("username"));
        	user.setEmail(rs.getString("email"));
        	user.setFirstName(rs.getString("first_name"));
        	user.setLastName(rs.getString("last_name"));
        	user.setCity(rs.getString("city"));
        	user.setPasswordHash(rs.getString("password_hash"));
        	user.setRegistrationDate(rs.getString("registration_date"));
        	if ( DatabaseManager.getType().equals("sqlite" ) ) {
        		rs.next();
        	}
        }
        return user;
    }
 
    public static ObservableList<RegisteredUser> getAllRegisteredUsers () throws SQLException {
    	var allUsersRs = DatabaseManager.getConnection().createStatement().executeQuery("SELECT * FROM registered_users");
    	return getRegisteredUsers( allUsersRs );
    }
 

    private static ObservableList<RegisteredUser> getRegisteredUsers(ResultSet rs) throws SQLException {
        ObservableList<RegisteredUser> userList = FXCollections.observableArrayList();
        var user = getRegisteredUserFromResultSet(rs);
        
        while (user != null) {
    		userList.add(user);
    		user = getRegisteredUserFromResultSet(rs);
        }
        
        return userList;
    }

    public static void updateRegisteredUser (RegisteredUser user) throws SQLException {
        String sql = "UPDATE registered_users "
			+ "SET username = ?,email = ?,first_name = ?,last_name = ?,city = ?,password_hash = ?,registration_date = ?"
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
 
    public static void insertRegisteredUser (RegisteredUser user) throws SQLException {
        var sql = "INSERT INTO registered_users " +
			"(username, email, first_name, last_name, city, password_hash, registration_date)" +
			"VALUES (?,?,?,?,?,?,?);";

        var insertUser = DatabaseManager.getConnection().prepareStatement(sql);
        insertUser.setString(1,  user.getUsername());
        insertUser.setString(2,  user.getEmail());
        insertUser.setString(3,  user.getFirstName());
        insertUser.setString(4,  user.getLastName());
        insertUser.setString(5,  user.getCity());
        insertUser.setString(6,  user.getPasswordHash());
        insertUser.setString(7,  user.getRegistrationDate());
        
        insertUser.executeUpdate();
  
        RegisteredUser insertedUser = findRegisteredUser("username",user.getUsername());
        user.setUserId(insertedUser.getUserId());
    }
}