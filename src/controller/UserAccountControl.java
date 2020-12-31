package controller;

import java.io.IOException;
import java.sql.SQLException;

import application.ApplicationSettings;
import application.DatabaseManager;
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
	
	private ObjectProperty<RegisteredUser> loggedInUser = new SimpleObjectProperty<>( new RegisteredUser() );
	private UserAccountRegisterForm registerForm;
	private UserAccountLoginForm loginForm;
	private UserAccountInfo accountInfoPane;

	public void initialize() {
		this.registerAccountMenu.setOnAction( e -> this.showRegisterForm() );
		this.connectMenu.setOnAction( e -> this.showLoginForm() );
		this.accountInfoMenu.setOnAction( e -> this.showAccountInfo() );
		
		this.quitMenu.setOnAction( e -> System.exit(0) );
		
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


    	this.connectedUserBar.getUserProperty().bind(this.loggedInUser);
    	this.connectedUserBar.getButton().setOnAction( e->this.loggedInUser.set(null) );
    	
		this.loggedInUser.addListener( (e,oldUser,newUser)-> {
			this.accountInfoMenu.setDisable(newUser == null);
			if ( newUser == null ) {
				ApplicationSettings.set("loggedInUserUsername", null);
				Platform.runLater( ()-> this.showLoginForm() );
			} else {
				ApplicationSettings.set("loggedInUserUsername", newUser.getUsername());
				ApplicationSettings.set("loggedInUserPasswordHash", newUser.getPasswordHash());
				Platform.runLater( ()-> this.showAccountInfo() );
			}
		} );
		
		this.loggedInUser.set(
    			RegisteredUserDAO.find( "username", ApplicationSettings.get("loggedInUserUsername") )
    	);

		var settingsDialog = new SettingsDialog();
		this.databaseSettingsMenu.setOnAction( e -> settingsDialog.show() );
		
		DatabaseManager.getConnectionParametersProperty().addListener((e,oldValue,newValue)->{
			System.out.println("Changed.");
			this.loggedInUser.set(null);
		});

	}
	
	public void showLoginForm(){
		if ( this.loginForm == null ) {
			this.loginForm = new UserAccountLoginForm();
			this.loginForm.getLoggedInUserProperty().bindBidirectional(this.loggedInUser);
		}
		this.setBottom(this.loginForm);
		this.getScene().getWindow().sizeToScene();
	}
	
	public void showRegisterForm(){
		if ( this.registerForm == null ) {
			this.registerForm = new UserAccountRegisterForm();
			this.registerForm.getLoggedInUserProperty().bindBidirectional(this.loggedInUser);
		}
		this.setBottom(this.registerForm);
		this.getScene().getWindow().sizeToScene();
	}
	
	public void showAccountInfo(){
		if ( this.accountInfoPane == null ) {
			this.accountInfoPane = new UserAccountInfo();
			this.accountInfoPane.getLoggedInUserProperty().bindBidirectional(this.loggedInUser);
		}
		this.accountInfoPane.reset();
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
}
