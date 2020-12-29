package controller;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import application.ApplicationSettings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.DatabaseManager;
import model.RegisteredUser;
import model.RegisteredUserDAO;

/**
 * Sets up main window and spawns menu, animal regions..
 * acts as a bridge between different controllers
 */
public class UserAccountControl extends BorderPane{
	@FXML private MenuItem  quitMenu, databaseSettingsMenu, databaseResetMenu, databaseRepopulateMenu, registerAccountMenu, connectMenu, accountInfoMenu;
	@FXML private RegisteredUser loggedInUser;

	private UserAccountRegisterForm registerForm;
	private UserAccountLoginForm loginForm;
	
	/*
	 * set up elements
	 * */
	public void initialize() {
		var settingsDialog = new SettingsDialog();
		this.registerAccountMenu.setOnAction( e -> this.showRegisterForm() );
		this.connectMenu.setOnAction( e -> this.showLoginForm() );
		this.accountInfoMenu.setOnAction( e -> {} );
		
		this.quitMenu.setOnAction( e -> System.exit(0) );
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
		

    	Platform.runLater( ()-> this.showRegisterForm() );

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
		setUpDatabase (
			ApplicationSettings.get("databaseType"),
			ApplicationSettings.get("databaseAddress"),
			ApplicationSettings.get("databaseName"),
			ApplicationSettings.get("databaseLogin"),
			ApplicationSettings.get("databasePassword")
		);
	}
	
	public void showLoginForm(){
		if ( this.loginForm == null ) {
			this.loginForm = new UserAccountLoginForm();
		}
		this.setBottom(this.loginForm);
		this.getScene().getWindow().sizeToScene();
	}
	
	public void showRegisterForm(){
		if ( this.registerForm == null ) {
			this.registerForm = new UserAccountRegisterForm();
		}
		this.setBottom(this.registerForm);
		this.getScene().getWindow().sizeToScene();
	}

	
	
	public static void setUpDatabase(String type, String address, String name, String login, String password) throws SQLException {
		
		var connURL= "jdbc:" + type + ":"
			+ address.replaceFirst("/*$", "") // remove trailing slashes from database address
			+ "/" + name;
		
    	if (type.equals("sqlite")) {
    		connURL += ".sqlite.db";
    	}
   
		DatabaseManager.setConnectionParameters( connURL, login, password );
		
		RegisteredUserDAO.createTable();
	}
}
