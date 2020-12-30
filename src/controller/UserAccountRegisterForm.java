package controller;


import java.io.IOException;
import java.sql.SQLException;

import form.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import model.*;

public class UserAccountRegisterForm extends GridPane{
    private UserAccountControl mainController;	// for access to other controllers
    @FXML private Text mainHeader;
    @FXML private BaseField fieldFirstName, fieldLastName, fieldCity;
    @FXML private ValidatedUsernameField fieldUsername;
    @FXML private ValidatedEmailField fieldEmail;
    @FXML private ValidatedPasswordField fieldPassword;
    @FXML private ValidatedBasePasswordField fieldConfirmPassword;
    
    @FXML private Button submitButton;
    
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
			RegisteredUserDAO.insert(newUser);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		this.getMainController().setLoggedInUser(newUser);
		this.getMainController().showAccountInfo();
    }
    
    // Interface for validator with one method passed in lambda function

    
    /**
     * Set up gui elements
     */
    @FXML private void initialize() {

    	this.fieldUsername.addValidator(
			(val) -> RegisteredUserDAO.find("username", val) == null,
			"Nom d'utilisateur libre."
		);
    	this.fieldEmail.addValidator(
			(val) -> RegisteredUserDAO.find("email", val) == null,
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
    

	/**
	 * Sets a reference to the main controller (for console, and to communicate with other controllers)
	 * @param c main window controller
	 */
	void setMainController(UserAccountControl c)
	{
		this.mainController = c;
	}

	UserAccountControl getMainController()
	{
		return this.mainController;
	}
}
