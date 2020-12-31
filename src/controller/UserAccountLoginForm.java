package controller;
import java.io.IOException;

import form.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import model.*;


public class UserAccountLoginForm extends GridPane{
	@FXML private BaseField fieldConnectionIdentifier;
	@FXML private ValidatedBasePasswordField fieldConnectionPassword;
	private ObjectProperty<RegisteredUser> loggedInUser = new SimpleObjectProperty<>();
    
    public final ObjectProperty<RegisteredUser> getLoggedInUserProperty() {
    	return this.loggedInUser;
    }

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
	
    @FXML protected void handleConnectButtonAction(ActionEvent event) {
    	RegisteredUser user;
    	
    	if ( fieldConnectionIdentifier.getText().contains("@")) {
    		user = RegisteredUserDAO.find("email", fieldConnectionIdentifier.getText());
    	} else {
    		user = RegisteredUserDAO.find("username", fieldConnectionIdentifier.getText());
    	}
    	
    	if (user == null) {
			Alert a = new Alert(AlertType.WARNING);
			a.setHeaderText("Identifiants incorrects");
			a.setContentText("Vérifiez l'exactitude de vos paramètres.");
			a.show();
    	} else {
    		this.loggedInUser.set(user);
    	}
    }

}
