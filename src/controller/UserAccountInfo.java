package controller;
import java.io.IOException;
import java.sql.SQLException;

import form.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import model.RegisteredUser;
import model.RegisteredUserDAO;


/**
 *	Account information pane that allows editing of user's data.
 */
public class UserAccountInfo extends ControlledGridPane{
    @FXML ValidatedUsernameField fieldModifyUsername;
    @FXML ValidatedEmailField fieldModifyEmail;
    @FXML BaseField fieldModifyFirstName, fieldModifyLastName, fieldModifyCity;
    @FXML ValidatedBasePasswordField fieldOldPassword;
    @FXML ValidatedPasswordField fieldModifyPassword;
    @FXML ValidatedConfirmPasswordField fieldConfirmModifyPassword;
    @FXML Label registrationDate;
    
    // Edited user is stored separately from logged in user, so that we can bind its properties to editable field.
    private ObjectProperty<RegisteredUser> editedUser = new SimpleObjectProperty<>();
    
	public void initialize() {
		// When main controller is changed, reset fields and set up listeners
		this.getMainControllerPropery().addListener((o,oldController,newController)->{
			this.reset();
	    	
			this.fieldOldPassword.addValidator(
    			(val) -> newController.getLoggedInUser().isPassword(val), // must match old password
    			"Correspond avec l'ancien mot de passe."
    		);
	    	this.fieldModifyUsername.addValidator( 
				(val) -> 	val.equals(newController.getLoggedInUser().getUsername()) // username should either unmodified, 
						|| 	RegisteredUserDAO.find("username", val) == null,		  // or not present in data source
				"Nom d'utilisateur libre."
			);
	    	this.fieldModifyEmail.addValidator(
				(val) ->	val.equals(newController.getLoggedInUser().getEmail())	// email should either unmodified, 
						||  RegisteredUserDAO.find("email", val) == null,			// or not present in data source
				"Email libre."
			);
	    	
	    	this.fieldModifyPassword.addSpecialCase( (val) -> val.length() == 0 ); // Accept blank new password (if unchanged)
		});
		
		// when fields are edited, make edited user properties change as well
		this.editedUser.addListener((e,oldValue,newValue)->{
			this.registrationDate.textProperty().bind(newValue.getRegistrationDateProperty());
			this.fieldModifyUsername.textProperty().bindBidirectional(newValue.getUsernameProperty());
			this.fieldModifyEmail.textProperty().bindBidirectional(newValue.getEmailProperty());
			this.fieldModifyFirstName.textProperty().bindBidirectional(newValue.getFirstNameProperty());
			this.fieldModifyLastName.textProperty().bindBidirectional(newValue.getLastNameProperty());
			this.fieldModifyCity.textProperty().bindBidirectional(newValue.getCityProperty());
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
    	if (this.fieldModifyPassword.getText().length()>0) { // Change password if field not left blank
    		this.editedUser.get().setPasswordHashFromPlainText(this.fieldModifyPassword.getText());
    	}
    	try {
			RegisteredUserDAO.update(this.editedUser.get()); // add edited user to data source

			this.getMainController().logInUser(
				this.editedUser.get().clone(),   // Clone to avoid them pointing to same ref
				this.fieldModifyPassword.getText()
			); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    
    /**
     * reset fields to currently logged in user info
     */
    public void reset() {
    	this.editedUser.set(this.getMainController().getLoggedInUser().clone());
    	this.fieldOldPassword.setText("");
    	this.fieldModifyPassword.setText("");
    	this.fieldConfirmModifyPassword.setText("");
    }
}
