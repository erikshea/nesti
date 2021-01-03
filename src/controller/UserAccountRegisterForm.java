package controller;


import java.io.IOException;
import java.sql.SQLException;

import form.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import model.*;

public class UserAccountRegisterForm extends ControlledGridPane{
    @FXML private Text mainHeader;
    @FXML private BaseField fieldFirstName, fieldLastName, fieldCity;
    @FXML private ValidatedUsernameField fieldUsername;
    @FXML private ValidatedEmailField fieldEmail;
    @FXML private ValidatedPasswordField fieldPassword;
    @FXML private ValidatedBasePasswordField fieldConfirmPassword;
    @FXML private Button submitButton;
    

    /**
     *  Submit button logic
     * @param event
     */
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
		var newUser = new RegisteredUser(
				fieldUsername.getText(),
				fieldEmail.getText(),
				fieldFirstName.getText(),
				fieldLastName.getText(),
				fieldCity.getText(),
				fieldPassword.getText()
		);
		try {
			RegisteredUserDAO.insert(newUser); // Add to data source
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.getMainController().logInUser(newUser, fieldPassword.getText()); // Log in with new user.
    }
    
    /**
     *  Connect button logic
     * @param event
     */
    @FXML protected void handleConnectButtonAction(ActionEvent event) {
		this.getMainController().showRegion( new UserAccountLoginForm() );
    }
    
    /**
     * Set up gui elements
     */
    @FXML private void initialize() {
    	this.fieldUsername.addValidator(
			(val) -> RegisteredUserDAO.find("username", val) == null, // Username mustn't exist in data source
			"Nom d'utilisateur libre."
		);
    	this.fieldEmail.addValidator(
			(val) -> RegisteredUserDAO.find("email", val) == null,	// Email mustn't exist in data source
			"Email libre."
		);
    }
    
    
    /**
     * Loads .fxml as root
     */
    public UserAccountRegisterForm(){
        var loader = new FXMLLoader(getClass().getResource("UserAccountRegisterForm.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
