package controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
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
import javafx.application.Platform;

public class UserAccountRegister extends GridPane{
    private MainWindowControl mainController;	// for access to other controllers
    private HashMap<TextField, Popup> validationPopups = new HashMap<>(); // Stores Popup objects because they aren't in Node tree.
    private static final int MIN_PASSWORD_STRENGTH=100;
    @FXML private Text welcomeText;
    @FXML private TextField fieldUsername, fieldEmail, fieldFirstName, fieldName, fieldCity;
    @FXML private PasswordField fieldPassword, fieldConfirmPassword;
    @FXML private Button submitButton;
    @FXML private ProgressBar passwordIndicator;
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
       // actiontarget.setText("Sign in button pressed");
    }
    
    // Interface for validator with one method passed in lambda function
	interface FieldValidator {
		  boolean validate(String fieldValue);
	}
    
    /**
     * Set up gui elements
     */
    @FXML private void initialize() {
    	this.fieldPassword.textProperty().addListener((observable, oldValue, newValue) -> {
        	Platform.runLater(()->{ // runLater needed to avoid IllegalStateException when running unit tests without FXRobot
        		double progress = getPasswordStrength(newValue)/(2*MIN_PASSWORD_STRENGTH);
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
    	});
    	
    	this.addFieldValidator( this.fieldEmail, null,
			(String value) -> value.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"),
			"Valide."); // email regex source: https://emailregex.com/
    	this.addFieldValidator( this.fieldUsername, null,
			(String value) -> value.matches("^[\\w-]+$"),
			"Constitué de lettres non-accentuées, de chiffres, et des signes moins et sous-tiret.");
    	this.addFieldValidator( this.fieldPassword, this.passwordIndicator,
			(String value) -> value.matches("^.*[0-9].*$"),
			"Contient au moins un chiffre.");
    	this.addFieldValidator( this.fieldPassword, null,
			(String value) -> value.matches("^.*[a-zA-Z].*$"),
			"Contient au moins une lettre.");
    	this.addFieldValidator( this.fieldPassword, null,
			(String value) -> ( UserAccountRegister.getPasswordStrength(value) > MIN_PASSWORD_STRENGTH ),
			"Suffisamment fort.");
    	this.addFieldValidator( fieldConfirmPassword, null,
    		(String value) -> value.equals(this.fieldPassword.getText()),
			"Correspond.");
    }
    
    private void addFieldValidator(TextField field, Region anchorNode, FieldValidator validator, String message) {
    	VBox popupContent = (VBox) this.getValidationPopup(field, anchorNode).getContent().get(0);
    	
        Label validatorLabel = new Label(message);
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
            Popup validationPopup = new Popup();
        	validationPopup.autoHideProperty().set(true); // hide on any key/mouse event
        	validationPopup.setConsumeAutoHidingEvents(false); // hiding key/mouse event can also focus another field or manipulate window
        	VBox messages = new VBox();
        	messages.getStyleClass().add("validationMessages");
            validationPopup.getContent().add(messages);
            
            this.validationPopups.put(field, validationPopup);
            
            final Region anchor = anchorNode==null?field:anchorNode;

        	field.focusedProperty().addListener((observable, oldValue, newValue) -> {
        		if (newValue) { 
                	Point2D screenPos = anchor.localToScreen(0.0,anchor.getHeight());
                	validationPopup.show(field.getScene().getWindow(), screenPos.getX(), screenPos.getY() );
        		}
        	});
        	
       
        	field.textProperty().addListener((observable, oldValue, newValue) -> {
            	Point2D screenPos = anchor.localToScreen(0.0,anchor.getHeight());
            	Platform.runLater(()->{ // runLater needed to avoid IllegalStateException when running unit tests without FXRobot
            		validationPopup.show(field.getScene().getWindow(), screenPos.getX(), screenPos.getY() );
            	});
        	});
    	}
    	
    	return this.validationPopups.get(field);
    }
    
    private static double getPasswordStrength(String password) {
    	int possibleChars = 0;
    	
    	ArrayList<char[]> checkRanges = new ArrayList<char[]>();
    	checkRanges.add(new char[]{'0','9'});
    	checkRanges.add(new char[]{'a','z'});
    	checkRanges.add(new char[]{'A','Z'});
    	
    	for ( char[] range: checkRanges) {
    		if (password.matches("^.*[" + range[0] + "-" + range[1] + "].*$")) {
    			possibleChars += range[1] - range[0] + 1;
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
    public UserAccountRegister(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userAccountRegister.fxml"));
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
	void setMainController(MainWindowControl c)
	{
		this.mainController = c;
	}

	
}
