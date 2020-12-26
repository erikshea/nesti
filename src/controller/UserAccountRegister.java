package controller;

import java.awt.Dimension;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.controlsfx.control.PopOver;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
public class UserAccountRegister extends GridPane{
    private MainWindowControl mainController;	// for access to other controllers
    private HashMap<TextField, Popup> validationPopups = new HashMap<>(); // Stores Popup objects because they aren't in Node tree.
    
    @FXML private Text welcomeText,actiontarget;
    @FXML private TextField fieldUsername, fieldEmail, fieldFirstName, fieldName, fieldCity;
    @FXML private PasswordField fieldPassword, fieldConfirmPassword;
    @FXML private Button submitButton;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }
    
    // Interface for validator with one method passed in lambda function
	interface FieldValidator {
		  boolean validate(String fieldValue);
	}
    
    /**
     * Set up gui elements
     */
    @FXML private void initialize() {
    	this.addFieldValidator( fieldEmail, null,
			(String value) -> value.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"),
			"Valide."); // email regex source: https://emailregex.com/
    	this.addFieldValidator( fieldUsername, null,
			(String value) -> value.matches("^.*\\w.*$"),
			"Constitué de chiffres, de lettres, et des signes moins et sous-tiret.");
    	this.addFieldValidator( fieldPassword, null,
			(String value) -> value.matches("^.*[0-9].*$"),
			"Contient au moins un chiffre.");
    	this.addFieldValidator( fieldPassword, null,
			(String value) -> value.matches("^.*[a-zA-Z].*$"),
			"Contient au moins une lettre.");
    	this.addFieldValidator( fieldPassword, null,
			(String value) -> ( UserAccountRegister.getPasswordStrength(value)>50 ),
			"Suffisamment fort.");
    	this.addFieldValidator( fieldConfirmPassword, null,
    		(String value) -> value.equals(fieldPassword.getText()),
			"Correspond avec le mot de passe.");
    }
    
    
    private void addFieldValidator(TextField field, Region anchorNode, FieldValidator validator, String message) {
    	if ( !this.validationPopups.containsKey(field) ) {
            Popup validationPopup = new Popup() {
				@Override
				public void show(Window w) {
		            Point2D fieldScreenPos;
		        	if (anchorNode != null) {
		        		fieldScreenPos = anchorNode.localToScreen(0.0,anchorNode.getHeight());
		        	} else {
		        		fieldScreenPos = field.localToScreen(0.0,field.getHeight());
		        	}
					super.show(w, fieldScreenPos.getX(), fieldScreenPos.getY());
				}
            };
            
        	validationPopup.autoHideProperty().set(true);
        	validationPopup.setConsumeAutoHidingEvents(false);
        	
        	field.focusedProperty().addListener((observable, oldValue, newValue) -> {
        		if (newValue) { 
                    validationPopup.show(field.getScene().getWindow());
        		}
        	});
        	
        	field.textProperty().addListener((observable, oldValue, newValue) -> {
        		validationPopup.show(field.getScene().getWindow());
        	});

    		field.getStyleClass().add("requiredField");
            validationPopup.getContent().add(new VBox());
            this.validationPopups.put(field, validationPopup);
    	}
    	
    	Popup validationPopup = this.validationPopups.get(field);
    	
    	VBox popupContent = (VBox) validationPopup.getContent().get(0);
        Label validatorLabel = new Label(message);
        validatorLabel.getStyleClass().add("validationLabel");
    	popupContent.getChildren().add(validatorLabel);

    	field.textProperty().addListener((observable, oldValue, newValue) -> {
    		validatorLabel.getStyleClass().remove("validLabel");;
            if(validator.validate(newValue)){
            	validatorLabel.getStyleClass().add("validLabel");
            }
            
            field.getStyleClass().remove("validField");
            if (popupContent.lookupAll(".validLabel").size() == popupContent.getChildren().size())
            {
            	field.getStyleClass().add("validField");
            }

            submitButton.setDisable(this.lookupAll(".requiredField.validField").size() != this.lookupAll(".requiredField").size() );
    	});
    }
    
    private static int getPasswordStrength(String password) {
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
            possibleChars += 100;
        }
        
        return (int) ( password.length() *  Math.log(possibleChars)/Math.log(2) );
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
