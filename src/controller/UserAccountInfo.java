package controller;
import java.io.IOException;
import java.sql.SQLException;

import form.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.RegisteredUserDAO;


public class UserAccountInfo extends GridPane{
    private ObjectProperty<UserAccountControl> mainController=new SimpleObjectProperty<>();	// for access to other controllers
    @FXML ValidatedUsernameField fieldModifyUsername;
    @FXML ValidatedEmailField fieldModifyEmail;
    @FXML BaseField fieldModifyFirstName, fieldModifyLastName, fieldModifyCity;
    @FXML ValidatedBasePasswordField fieldOldPassword;
    @FXML ValidatedPasswordField fieldModifyPassword;
    @FXML ValidatedConfirmPasswordField fieldConfirmModifyPassword;
    @FXML Text registrationDate;
    
    
	/**
	 * Sets a reference to the main controller (for console, and to communicate with other controllers)
	 * @param c main window controller
	 */
	void setMainController(UserAccountControl c)
	{
		this.mainController.set(c);
	}
	
	UserAccountControl getMainController()
	{
		return this.mainController.get();
	}

	public void initialize() {
		this.mainController.addListener((e,oldValue,newValue)->{
	    	Platform.runLater(()->{ // Wait for loading of fields
	    		var user = this.getMainController().getLoggedInUser();

	    		this.fieldOldPassword.addValidator(
	    			(val) -> user.isPassword(val),
	    			"Correspond avec l'ancien mot de passe."
	    		);
	        	this.fieldModifyUsername.addValidator(
        			(val) -> 	user.getUsername().equals(val)
        					|| 	RegisteredUserDAO.find("username", val) == null,
        			"Nom d'utilisateur libre."
        		);
            	this.fieldModifyEmail.addValidator(
        			(val) ->	user.getEmail().equals(val)
        					||  RegisteredUserDAO.find("email", val) == null,
        			"Email libre."
        		);
            	
            	this.fieldModifyPassword.addSpecialCase(
        			(val) -> val.length() == 0
        		);
            	this.fieldConfirmModifyPassword.applyValidators();
            	
            	this.registrationDate.setText(user.getRegistrationDate());
	    		this.fieldModifyUsername.setText(user.getUsername());
	    		this.fieldModifyEmail.setText(user.getEmail());
	    		this.fieldModifyFirstName.setText(user.getFirstName());
	    		this.fieldModifyLastName.setText(user.getLastName());
	    		this.fieldModifyCity.setText(user.getCity());
	    		
	    	});
		});
		

	}
	
	public UserAccountInfo(){
        var loader = new FXMLLoader(getClass().getResource("UserAccountInfo.fxml"));	
        loader.setRoot(this);																	
        loader.setController(this); // register as fxml root controller 
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	
    @FXML protected void handleSaveButtonAction(ActionEvent event) {
		var user = this.getMainController().getLoggedInUser();
    	user.setUsername(this.fieldModifyUsername.getText());
    	user.setEmail(this.fieldModifyEmail.getText());
    	user.setFirstName(this.fieldModifyFirstName.getText());
    	user.setLastName(this.fieldModifyLastName.getText());
    	user.setCity(this.fieldModifyCity.getText());
    	if (this.fieldModifyPassword.getText().length()>0) {
        	user.setPasswordHashFromPlainText(this.fieldModifyPassword.getText());
    	}
    	try {
			RegisteredUserDAO.update(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
