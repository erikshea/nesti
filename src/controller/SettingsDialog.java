package controller;

import java.io.IOException;
import java.sql.SQLException;

import application.ApplicationSettings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import model.DatabaseManager;
import javafx.event.ActionEvent;

public class SettingsDialog extends Dialog<Boolean> {
	private UserAccountControl mainController;
	@FXML private ButtonType okButton, defaultsButton, saveButton;
	@FXML private TextField settingsAddress, settingsDatabaseName, settingsLogin, settingsPassword;
	@FXML ComboBox<String> databaseType;
	public void setMainController(UserAccountControl mainController) {
		this.mainController = mainController;
	}
	
	public SettingsDialog(){
        var loader = new FXMLLoader(getClass().getResource("SettingsDialog.fxml"));	// load .fxml 
        loader.setRoot(this);																	// as root
        loader.setController(this); // register as its controller 

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        // Fix to allow "X" button to close dialog with no "CANCEL_CLOSE" buttontype
        this.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> this.hide());

        this.setOnShowing(e->{
        	databaseType.setValue(ApplicationSettings.get("databaseType"));
        	settingsAddress.setText(ApplicationSettings.get("databaseAddress"));
        	settingsDatabaseName.setText(ApplicationSettings.get("databaseName"));
        	settingsLogin.setText(ApplicationSettings.get("databaseLogin"));
        	settingsPassword.setText(ApplicationSettings.get("databasePassword"));
        });
        
        this.getDialogPane().lookupButton(defaultsButton).addEventFilter(ActionEvent.ACTION, event -> {
        	databaseType.setValue(ApplicationSettings.getDefault("databaseType"));
        	settingsAddress.setText(ApplicationSettings.getDefault("databaseAddress"));
        	settingsDatabaseName.setText(ApplicationSettings.getDefault("databaseName"));
        	settingsLogin.setText(ApplicationSettings.getDefault("databaseLogin"));
        	settingsPassword.setText(ApplicationSettings.getDefault("databasePassword"));
            event.consume();
        });
        
        this.getDialogPane().lookupButton(saveButton).addEventFilter(ActionEvent.ACTION, event -> {
	    	try {
	    		DatabaseManager.setConnectionParameters(
					databaseType.getValue().toString(),
					settingsAddress.getText(),
					settingsDatabaseName.getText(),
					settingsLogin.getText(),
					settingsPassword.getText()
				);
	    		ApplicationSettings.set("databaseType", databaseType.getValue().toString());
	        	ApplicationSettings.set("databaseAddress", settingsAddress.getText());
	        	ApplicationSettings.set("databaseName", settingsDatabaseName.getText());
	        	ApplicationSettings.set("databaseLogin", settingsLogin.getText());
	        	ApplicationSettings.set("databasePassword", settingsPassword.getText());
	        	this.mainController.disconnectUser();
	    	} catch (Exception e) {
				e.printStackTrace();
				Alert a = new Alert(AlertType.WARNING);
				a.setHeaderText("Problème de connection");
				a.setContentText("Impossible d'établir une connection.");
				a.show();
				try {
					UserAccountControl.setUpDatabaseFromSettings();
				} catch (SQLException e2) {}
				event.consume();
			}
        });
	}
}
