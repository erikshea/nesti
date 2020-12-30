package controller;

import java.io.IOException;
import java.sql.SQLException;

import application.ApplicationSettings;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import model.*;

/**
 * Sets up main window and spawns menu, animal regions..
 * acts as a bridge between different controllers
 */
public class UserAccountControl extends BorderPane{
	@FXML private MenuItem  quitMenu, databaseSettingsMenu, databaseResetMenu, databaseRepopulateMenu, registerAccountMenu, connectMenu, accountInfoMenu;
	@FXML ConnectedUserBar connectedUserBar;
	
	private ObjectProperty<RegisteredUser> loggedInUser = new SimpleObjectProperty<RegisteredUser>(new RegisteredUser());
	private UserAccountRegisterForm registerForm;
	private UserAccountLoginForm loginForm;
	private UserAccountInfo accountInfoPane;
	
	/*
	 * set up elements
	 * */
	public void initialize() {
		this.getloggedInUserProperty().addListener( (e,oldUser,newUser)-> {
			this.accountInfoMenu.setDisable(this.getLoggedInUser() == null);
		} );
		

		
		this.registerAccountMenu.setOnAction( e -> this.showRegisterForm() );
		this.connectMenu.setOnAction( e -> this.showLoginForm() );
		this.accountInfoMenu.setOnAction( e -> this.showAccountInfo() );
		
		this.quitMenu.setOnAction( e -> System.exit(0) );
		
		var settingsDialog = new SettingsDialog();
		settingsDialog.setMainController(this);
		this.databaseSettingsMenu.setOnAction( e -> settingsDialog.show() );
		
		this.databaseResetMenu.setOnAction( e -> {
			try {
				RegisteredUserDAO.resetTable();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
		
		this.databaseRepopulateMenu.setOnAction( e -> {
			try {
				RegisteredUserDAO.populateTable();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
		
		try {
			setUpDatabaseFromSettings();
		} catch (SQLException e) {}

    	this.connectedUserBar.setMainController(this);
    	
    	this.setLoggedInUser(
    			RegisteredUserDAO.find( "username", ApplicationSettings.get("loggedInUserUsername") )
    	);
    	Platform.runLater( ()-> this.showLoginForm() );
	}
	
	public void showLoginForm(){
		if ( this.loginForm == null ) {
			this.loginForm = new UserAccountLoginForm();
			this.loginForm.setMainController(this);
		}
		this.setBottom(this.loginForm);
		this.getScene().getWindow().sizeToScene();
	}
	
	public void showRegisterForm(){
		if ( this.registerForm == null ) {
			this.registerForm = new UserAccountRegisterForm();
			this.registerForm.setMainController(this);
		}
		this.setBottom(this.registerForm);
		this.getScene().getWindow().sizeToScene();
	}
	
	public void showAccountInfo(){
		if ( this.accountInfoPane == null ) {
			this.accountInfoPane = new UserAccountInfo();
			this.accountInfoPane.setMainController(this);
		}
		this.setBottom(this.accountInfoPane);
		this.getScene().getWindow().sizeToScene();
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
	

	final public ObjectProperty<RegisteredUser> getloggedInUserProperty() {
		return this.loggedInUser;
	}
	
	public RegisteredUser getLoggedInUser() {
		return this.loggedInUser.get();
	}
	
	public void disconnectUser() {
		this.setLoggedInUser(null);
		
		this.showLoginForm();
	}
	
	
	public void setLoggedInUser( RegisteredUser user ) {
		if ( user == null ) {
			ApplicationSettings.set("loggedInUserUsername", null);
		} else {
			ApplicationSettings.set("loggedInUserUsername", user.getUsername());
			ApplicationSettings.set("loggedInUserPasswordHash", user.getPasswordHash());
		}
		
		this.loggedInUser.set(user);
	}
	
}
