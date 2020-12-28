package controller;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import model.DatabaseManager;
import model.RegisteredUser;
import model.RegisteredUserDAO;

/**
 * Sets up main window and spawns menu, animal regions..
 * acts as a bridge between different controllers
 */
public class UserAccountControl extends BorderPane{
	@FXML private MenuItem  quit;
	@FXML private UserAccountRegisterForm registerForm;
	
	/*
	 * set up game elements (those not in the .fxml) 
	 * */
	public void initialize() {
		//DatabaseManager.setConnectionParameters("jdbc:sqlite:./assets/database/nesti.sqlite.db","root","");

		DatabaseManager.setConnectionParameters("jdbc:mysql://127.0.0.1/nesti","root","");
		
		
		
		try {
			createRegisteredUserTable();
			var user = new RegisteredUser();

			user.setUsername("test");
			user.setEmail("tes@t.c");
			

			user.setPasswordHashFromPlainText("dssqsd");

			RegisteredUserDAO.insertRegisteredUser(user);
			System.out.println(user.getEmail());
			user.setEmail("dddd@ddd.dddd");
			RegisteredUserDAO.updateRegisteredUser(user);
			System.out.println(user.getEmail());
		} catch (SQLException e) {
			e.printStackTrace();
		}
        

	}

	public UserAccountControl(){
        var loader = new FXMLLoader(getClass().getResource("UserAccountControl.fxml"));	// load .fxml 
        loader.setRoot(this);																	// as root
        loader.setController(this); // register as its controller 

        	
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}

	private static void createRegisteredUserTable() throws SQLException {
		DatabaseManager.getConnection().createStatement().execute("DROP TABLE IF EXISTS registered_users;");
		DatabaseManager.getConnection().createStatement().execute("CREATE TABLE registered_users ("
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
}
