package controller;
import java.io.IOException;

import form.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.*;


/**
 *	Login form
 */
public class UserAccountLoginForm extends ControlledGridPane{
	@FXML private BaseField fieldConnectionIdentifier;
	@FXML private ValidatedBasePasswordField fieldConnectionPassword;

	public UserAccountLoginForm(){
        var loader = new FXMLLoader(getClass().getResource("UserAccountLoginForm.fxml"));	
        loader.setRoot(this);																	
        loader.setController(this); // register as fxml root controller 
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
    /**
     * Connection button logic. 
     * @param event
     */
    @FXML protected void handleConnectButtonAction(ActionEvent event) {
    	RegisteredUser user;
    	
    	if ( fieldConnectionIdentifier.getText().contains("@")) { // If email entered
    		user = RegisteredUserDAO.find("email", fieldConnectionIdentifier.getText());
    	} else {// Username entered
    		user = RegisteredUserDAO.find("username", fieldConnectionIdentifier.getText());
    	}
    	
    	if (user == null || !user.isPassword(this.fieldConnectionPassword.getText())) { // If user doesn't exist, or wrong password
    		// Show alert.
			Alert a = new Alert(AlertType.WARNING);
			a.setHeaderText("Identifiants incorrects");
			a.setContentText("Vérifiez vos paramètres.");
			a.show();
    	} else { // If information matches a user in data source
    		this.getMainController().logInUser(user, this.fieldConnectionPassword.getText());
    	}
    }

    /**
     * Register button logic. 
     * @param event
     */
    @FXML protected void handleRegisterButtonAction(ActionEvent event) {
    	this.getMainController().showRegion(new UserAccountRegisterForm());
    }
}
