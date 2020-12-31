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
import model.RegisteredUser;
import model.RegisteredUserDAO;


public class UserAccountInfo extends GridPane{
    @FXML ValidatedUsernameField fieldModifyUsername;
    @FXML ValidatedEmailField fieldModifyEmail;
    @FXML BaseField fieldModifyFirstName, fieldModifyLastName, fieldModifyCity;
    @FXML ValidatedBasePasswordField fieldOldPassword;
    @FXML ValidatedPasswordField fieldModifyPassword;
    @FXML ValidatedConfirmPasswordField fieldConfirmModifyPassword;
    @FXML Text registrationDate;
    
	private ObjectProperty<RegisteredUser> loggedInUser = new SimpleObjectProperty<>(new RegisteredUser());
    private ObjectProperty<RegisteredUser> editedUser = new SimpleObjectProperty<>();


	public void initialize() {
		this.fieldOldPassword.addValidator(
    			(val) -> this.loggedInUser.get().isPassword(val),
    			"Correspond avec l'ancien mot de passe."
    		);
    	this.fieldModifyUsername.addValidator(
			(val) -> 	val.equals(loggedInUser.get().getUsername())
					|| 	RegisteredUserDAO.find("username", val) == null,
			"Nom d'utilisateur libre."
		);
    	this.fieldModifyEmail.addValidator(
			(val) ->	val.equals(loggedInUser.get().getEmail())
					||  RegisteredUserDAO.find("email", val) == null,
			"Email libre."
		);
    	
    	this.fieldModifyPassword.addSpecialCase(
			(val) -> val.length() == 0
		);
		
		this.loggedInUser.addListener((e,oldValue,newValue)->{
			if (newValue != null ) {
				this.editedUser.set(newValue.clone());
			}
		});
		
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
    	if (this.fieldModifyPassword.getText().length()>0) {
    		this.editedUser.get().setPasswordHashFromPlainText(this.fieldModifyPassword.getText());
    	}
    	try {
			RegisteredUserDAO.update(this.editedUser.get());
			this.loggedInUser.set(this.editedUser.get());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public final ObjectProperty<RegisteredUser> getLoggedInUserProperty() {
    	return this.loggedInUser;
    }
    
    public void reset() {
    	this.editedUser.set(this.loggedInUser.get().clone());
    }
}
