package com.erikshea.nestiuseraccount.controller;
import com.erikshea.nestiuseraccount.model.RegisteredUser;
import com.erikshea.nestiuseraccount.model.RegisteredUserDAO;
import com.erikshea.nestiuseraccount.application.ApplicationSettings;
import com.erikshea.nestiuseraccount.application.DatabaseManager;
import java.io.IOException;
import java.sql.SQLException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 * Sets up main window, sets up and shows other controllers
 */
public class UserAccountControl extends BorderPane{
	// top menus
	@FXML private MenuItem  quitMenu, databaseSettingsMenu, databaseResetMenu, databaseRepopulateMenu, registerAccountMenu, connectMenu, accountInfoMenu;
	@FXML ConnectedUserBar connectedUserBar;	// user bar showing user currently logged in
	
	// observable: currently logged in user
	private ObjectProperty<RegisteredUser> loggedInUser = new SimpleObjectProperty<>( new RegisteredUser() );

	public void initialize() {
		this.setUpMenus(); // Set up listeners for menu items
    	this.connectedUserBar.setMainController(this);


		try {
			setUpDatabaseFromSettings(); // Load connection parameters from settings and connect
  
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		// Load logged in user from previous session
		var previousUser = RegisteredUserDAO.find( "username", ApplicationSettings.get("loggedInUsername") );
		if (previousUser != null && previousUser.isPassword(ApplicationSettings.get("loggedInPassword"))) { // Check stored plaintext password
			this.logInUser(previousUser,""); // Log in user, don't modify password in settings
		} else {
			this.logOutUser(); // run log out user logic
		}
		
		
		this.bottomProperty().addListener((e,o,newRegion)->{
			this.getScene().getWindow().sizeToScene(); // resize window on changes of bottom pane
			newRegion.requestFocus(); // don't focus first field
		});
		
		// If data source changes, log out user
		DatabaseManager.getConnectionParametersProperty().addListener( (e,o,n)-> this.logOutUser() );
	
	}
	
	/**
	 * Set main to a region main windows bottom pane 
	 * @param ControlledGridPane region to show
	 */
	public void showRegion(ControlledGridPane region) {
		region.setMainController(this);
		this.setBottom(region);	// show region in bottom of main window pane
	}
	
	/**
	 *  Set up menu items
	 */
	public void setUpMenus() {
		this.quitMenu.setOnAction( e -> System.exit(0) );
		
		this.databaseResetMenu.setOnAction( e -> {
			try {
				RegisteredUserDAO.resetTable();
			} catch (SQLException SQLe) {
				SQLe.printStackTrace();
			}
			this.logOutUser();
		});
		
		this.databaseRepopulateMenu.setOnAction( e -> {
			try {
				RegisteredUserDAO.populateTable();
			} catch (SQLException SQLe) {
				SQLe.printStackTrace();
			}
			this.logOutUser();
		});

		this.databaseSettingsMenu.setOnAction( e -> (new SettingsDialog()).show() );
	}
	
	
	public UserAccountControl(){
        var loader = new FXMLLoader(getClass().getResource("UserAccountControl.fxml"));	
        loader.setRoot(this);																	
        loader.setController(this); // register as fxml root controller 
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}

	/**
	 * Sets up database connection from application settings
	 * @throws SQLException
	 */
	public static void setUpDatabaseFromSettings() throws SQLException {
		DatabaseManager.setConnectionParameters(
			ApplicationSettings.get("databaseType"),
			ApplicationSettings.get("databaseAddress"),
			ApplicationSettings.get("databaseName"),
			ApplicationSettings.get("databaseLogin"),
			ApplicationSettings.get("databasePassword")
		);
		RegisteredUserDAO.createTable();
	}
	
	/**
	 * Log in user, update app session settings, open information pane
	 * @param user to log in
	 * @param plaintextPassword plaintext password to store in session settings
	 */
	public void logInUser(RegisteredUser user, String plaintextPassword) {
		this.loggedInUser.set(user); // set observable property
		ApplicationSettings.set("loggedInUsername", user.getUsername()); // store username to app settings
		if ( plaintextPassword.length() > 0) { // if password left blank, don't change it
			ApplicationSettings.set("loggedInPassword", plaintextPassword);
		}
		this.showRegion( new UserAccountInfo() ); // Show account info pane
	}
	
	/**
	 * Log out user and remove app session settings
	 */
	public void logOutUser() {
		this.loggedInUser.set(null);
		ApplicationSettings.set("loggedInUsername", null);
		ApplicationSettings.set("loggedInPassword", null);
		this.showRegion( new UserAccountLoginForm() );	// Show login form
	}
	
	public RegisteredUser getLoggedInUser() {
		return this.loggedInUser.get();
	}
	
	public final ObjectProperty<RegisteredUser> getLoggedInUserProperty() {
		return this.loggedInUser;
	}
}
