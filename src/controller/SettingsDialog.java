package controller;

import java.io.IOException;
import java.sql.SQLException;

import application.ApplicationSettings;
import application.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

/**
 * Shows database settings, allowing to switch between data sources
 */
public class SettingsDialog extends Dialog<Boolean> {
	@FXML private ButtonType okButton, defaultsButton, saveButton; // dialog buttons
	@FXML private TextField settingsAddress, settingsDatabaseName, settingsLogin, settingsPassword;
	@FXML ComboBox<String> databaseType; // eg. mysql, sqlite
	
	/**
	 *  Set up dialog window
	 */
	public SettingsDialog(){
        var loader = new FXMLLoader(getClass().getResource("SettingsDialog.fxml"));	// load .fxml 
        loader.setRoot(this); // as root
        loader.setController(this); // register as its controller 

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        // Fix to allow "X" button to close dialog with no "CANCEL_CLOSE" buttontype
        this.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> this.hide());

        this.setOnShowing(e->{ // On display, frill fields from settings
        	databaseType.setValue(ApplicationSettings.get("databaseType"));
        	settingsAddress.setText(ApplicationSettings.get("databaseAddress"));
        	settingsDatabaseName.setText(ApplicationSettings.get("databaseName"));
        	settingsLogin.setText(ApplicationSettings.get("databaseLogin"));
        	settingsPassword.setText(ApplicationSettings.get("databasePassword"));
        });
        
        // default button fills fields from default settings
        this.getDialogPane().lookupButton(defaultsButton).addEventFilter(ActionEvent.ACTION, event -> {
        	databaseType.setValue(ApplicationSettings.getDefault("databaseType"));
        	settingsAddress.setText(ApplicationSettings.getDefault("databaseAddress"));
        	settingsDatabaseName.setText(ApplicationSettings.getDefault("databaseName"));
        	settingsLogin.setText(ApplicationSettings.getDefault("databaseLogin"));
        	settingsPassword.setText(ApplicationSettings.getDefault("databasePassword"));
            event.consume();
        });
        
        // save button logic
        this.getDialogPane().lookupButton(saveButton).addEventFilter(ActionEvent.ACTION, event -> {
	    	try {
	    		DatabaseManager.setConnectionParameters( // Try to connect with new parameters
					databaseType.getValue().toString(),
					settingsAddress.getText(),
					settingsDatabaseName.getText(),
					settingsLogin.getText(),
					settingsPassword.getText()
				);
	    		// If no exception, commit them to settings
	    		ApplicationSettings.set("databaseType", databaseType.getValue().toString());
	        	ApplicationSettings.set("databaseAddress", settingsAddress.getText());
	        	ApplicationSettings.set("databaseName", settingsDatabaseName.getText());
	        	ApplicationSettings.set("databaseLogin", settingsLogin.getText());
	        	ApplicationSettings.set("databasePassword", settingsPassword.getText());
	        	UserAccountControl.setUpDatabaseFromSettings(); // Initialize with required tables.
	    	} catch (Exception e) {
				Alert a = new Alert(AlertType.WARNING); // On exception, show warning dialog
				a.setTitle("Impossible d'établir une connexion");
				a.setHeaderText("Erreur lors de la connexion:");
				a.setContentText( e.getLocalizedMessage() ); // Show localized error message
				a.show();
				try {
					UserAccountControl.setUpDatabaseFromSettings(); // Re-set parameters from previously valid settings
				} catch (SQLException e2) {e2.printStackTrace();}
				event.consume(); // Don't close settings dialog on connection error
			}
        });
	}
}
