package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import application.*;
import model.*;
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
			setUpDatabaseFromSettings(); // Load connection parameters from settings, connect
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		// Load logged in user from previous session
		var previousUser = RegisteredUserDAO.find( "username", ApplicationSettings.get("loggedInUsername") );
		if (previousUser != null && previousUser.isPassword(ApplicationSettings.get("loggedInPassword"))) { // Check stored plainText password
			this.logInUser(previousUser,""); // LOg in user, don't modify password in settings
		} else {
			this.logOutUser(); // run log out user logic
		}
		
		//resize window on subsequent changes of bottom pane
		this.bottomProperty().addListener((e)-> this.getScene().getWindow().sizeToScene());
		
		// If data source changes, log out user
		DatabaseManager.getConnectionParametersProperty().addListener( (e,o,n)-> this.logOutUser() );
	
	}
	
	/**
	 * Show region to fill window below menus and connected user bar)
	 * @param region region to show
	 */
	public void showRegion(ControlledGridPane region) {
		region.setMainController(this);
		this.setBottom(region);	// show region in bottom of main window pane
	}
	
	/**
	 *  Set up menu items
	 */
	public void setUpMenus() {
		this.registerAccountMenu.setOnAction( e -> this.showRegion( new UserAccountRegisterForm() ) );
		this.connectMenu.setOnAction( e -> this.showRegion( new UserAccountLoginForm() ) );
		
		this.accountInfoMenu.setOnAction( e -> this.showRegion( new UserAccountInfo() ) );
		this.getLoggedInUserProperty().addListener( (e,old,newUser) -> this.accountInfoMenu.setDisable( newUser == null ) );
		
		this.quitMenu.setOnAction( e -> System.exit(0) );
		
		this.databaseResetMenu.setOnAction( e -> {
			try {
				RegisteredUserDAO.resetTable();
				this.loggedInUser.set(null);
			} catch (SQLException SQLe) {
				SQLe.printStackTrace();
			}
		});
		
		this.databaseRepopulateMenu.setOnAction( e -> {
			try {
				RegisteredUserDAO.populateTable();
				this.loggedInUser.set(null);
			} catch (SQLException SQLe) {
				SQLe.printStackTrace();
			}
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
	 * @param plaintextPassword plaintext password to check user against
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
