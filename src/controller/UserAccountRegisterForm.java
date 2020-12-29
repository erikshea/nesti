package controller;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import form.ValidatedField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import model.RegisteredUser;
import model.RegisteredUserDAO;

public class UserAccountRegisterForm extends GridPane{
    private static final int MIN_PASSWORD_STRENGTH=100;
    private UserAccountControl mainController;	// for access to other controllers
    private HashMap<TextField, Popup> validationPopups = new HashMap<>(); // Stores validation Popup objects
    @FXML private Text mainHeader;
    @FXML private TextField fieldUsername, fieldEmail, fieldFirstName, fieldLastName, fieldCity;
    @FXML private PasswordField fieldPassword, fieldConfirmPassword;
    @FXML private Button submitButton;
    @FXML private ProgressBar passwordIndicator;
	@FXML private ValidatedField testField;
    
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
    }
    
    // Interface for validator with one method passed in lambda function

    
    /**
     * Set up gui elements
     */
    @FXML private void initialize() {
    	List.of("test","test2","test3").forEach( System.out::println );
    	
    	// Password strength bar updates when password changes
    	this.fieldPassword.textProperty().addListener((observable, oldValue, newValue) -> {
    		var progress = passwordStrength(newValue)/(2*MIN_PASSWORD_STRENGTH);
    		this.passwordIndicator.setProgress(progress);
    		this.passwordIndicator.getStyleClass().removeAll("great", "good", "poor", "very-poor");
    		if(progress > 0.75) {
    			this.passwordIndicator.getStyleClass().add("great");
    		} else if (progress > 0.5) {
    			this.passwordIndicator.getStyleClass().add("good");
    		}else if (progress > 0.25){
    			this.passwordIndicator.getStyleClass().add("poor");
    		}else {
    			this.passwordIndicator.getStyleClass().add("very-poor");
    		}
    	});


    	this.addFieldValidators();

    }
	interface FieldValidator {
		  boolean validate(String fieldValue);
	}
    private void addFieldValidators() {
    	this.addFieldValidator( this.fieldEmail, null,
			(val) -> val.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"),
			"Valide."); // email regex source: https://emailregex.com/
    	this.addFieldValidator( this.fieldEmail, null,
			(val) -> RegisteredUserDAO.find("email", val) == null,
			"Email libre.");
    	this.addFieldValidator( this.fieldUsername, null,
			(val) -> val.matches("^[\\w-]+$"),
			"Constitué de lettres non-accentuées, de chiffres, et des signes moins et sous-tiret.");
    	this.addFieldValidator( this.fieldUsername, null,
			(val) -> RegisteredUserDAO.find("username", val) == null,
			"Nom d'utilisateur libre.");
    	this.addFieldValidator( this.fieldPassword, this.passwordIndicator,
			(val) -> val.matches("^.*[0-9].*$"),
			"Contient au moins un chiffre.");
    	this.addFieldValidator( this.fieldPassword, null,
			(val) -> val.matches("^.*[a-zA-Z].*$"),
			"Contient au moins une lettre.");
    	this.addFieldValidator( this.fieldPassword, null,
			(val) -> ( passwordStrength(val) >= MIN_PASSWORD_STRENGTH ),
			"Suffisamment fort.");
    	this.addFieldValidator( fieldConfirmPassword, null,
    		(val) -> val.equals(this.fieldPassword.getText()),
			"Correspond.");
    }
    
    
    private void addFieldValidator(TextField field, Region anchorNode, FieldValidator validator, String message) {
    	var popupContent = (VBox) this.getValidationPopup(field, anchorNode).getContent().get(0);
    	
        var validatorLabel = new Label(message);
    	popupContent.getChildren().add(validatorLabel);
    	
    	field.textProperty().addListener((observable, oldValue, newValue) -> {
    		validatorLabel.getStyleClass().remove("valid");;
            if(validator.validate(newValue)){
            	validatorLabel.getStyleClass().add("valid");
            }
            
            field.getStyleClass().remove("valid");
            if (popupContent.lookupAll(".validationMessages Label.valid").size() == popupContent.lookupAll(".validationMessages Label").size())
            {
            	field.getStyleClass().add("valid");
            }

            submitButton.setDisable(this.lookupAll(".requiredField.valid").size() != this.lookupAll(".requiredField").size() );
    	});
    }
    
    
    private Popup getValidationPopup(TextField field, Region anchorNode) {
    	if ( !this.validationPopups.containsKey(field) ) {
    		field.getStyleClass().add("requiredField");
            var validationPopup = new Popup();
        	validationPopup.autoHideProperty().set(true); // hide on any key/mouse event
        	validationPopup.setConsumeAutoHidingEvents(false); // hiding key/mouse event can also focus another field or manipulate window
        	var messages = new VBox();
        	messages.getStyleClass().add("validationMessages");
            validationPopup.getContent().add(messages);
            
            this.validationPopups.put(field, validationPopup);
            
            final var anchor = anchorNode==null?field:anchorNode;

        	field.focusedProperty().addListener((observable, oldValue, newValue) -> {
        		if (newValue) { 
                	var screenPos = anchor.localToScreen(0.0,anchor.getHeight());
                	validationPopup.show(field.getScene().getWindow(), screenPos.getX(), screenPos.getY() );
        		} else {
                	validationPopup.hide(); // Must specify because auto-hide won't trigger on field traversal with TAB key
        		}
        	});
        	
       
        	field.textProperty().addListener((observable, oldValue, newValue) -> {
            	var screenPos = anchor.localToScreen(0.0,anchor.getHeight());
            		validationPopup.show(field.getScene().getWindow(), screenPos.getX(), screenPos.getY() );
        	});
        	
    	}
    	
    	return this.validationPopups.get(field);
    }
    
    private static double passwordStrength(String password) {
    	var possibleChars = 0;
    	
    	for ( var checkRange:List.of("09", "az", "AZ") ) {
    		if (password.matches("^.*[" + checkRange.charAt(0) + "-" + checkRange.charAt(1) + "].*$")) {
    			possibleChars += checkRange.charAt(1) - checkRange.charAt(0) + 1;
    		}
    	}
    	
        if ( password.matches("^.*\\W.*$") ) { // caractère spécial
            possibleChars += 50;
        }
        
        return password.length() *  Math.log(possibleChars)/Math.log(2);
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

	
}
