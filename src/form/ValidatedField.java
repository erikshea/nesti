package form;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;


import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;



public class ValidatedField  extends BaseField{
	public Popup validationPopup = null;
	public Region validationPopupAnchor = null;
	private Button buttonToDisable = null;
	private Region form;
	
	interface FieldValidator {
		  boolean validate(String fieldValue);
	}
    
    public void setDisableButton(String submitButtonId) {
    	this.buttonToDisable = (Button) this.getScene().lookup(submitButtonId);
    }
	
	
	public ValidatedField(){
		super();
		this.field.getStyleClass().add("requiredField");
		Platform.runLater( ()-> this.form = (Region) this.getScene().lookup(".validatedForm") );
	}
    
    public void addValidator(FieldValidator validator, String message) {
    	var popupContent = (VBox) this.getValidationPopup().getContent().get(0);
    	
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
            	this.field.getStyleClass().add("valid");
            }
            if (this.buttonToDisable != null) {
            	this.buttonToDisable.setDisable(this.form.lookupAll(".requiredField.valid").size() != this.form.lookupAll(".requiredField").size() );
            }
           
    	});
    }
    
    
    private Popup getValidationPopup() {
    	if ( this.validationPopup == null ) {
    		this.validationPopup = new Popup();
    		this.validationPopup.autoHideProperty().set(true); // hide on any key/mouse event
    		this.validationPopup.setConsumeAutoHidingEvents(false); // hiding key/mouse event can also focus another field or manipulate window
        	var messages = new VBox();
        	messages.getStyleClass().add("validationMessages");
        	this.validationPopup.getContent().add(messages);
            
            final var anchor = this.validationPopupAnchor==null?this.field:this.validationPopupAnchor;

            this.field.focusedProperty().addListener((observable, oldValue, newValue) -> {
        		if (newValue) { 
                	var screenPos = anchor.localToScreen(0.0,anchor.getHeight());
                	this.validationPopup.show(this.field.getScene().getWindow(), screenPos.getX(), screenPos.getY() );
        		} else {
        			this.validationPopup.hide(); // Must specify because auto-hide won't trigger on field traversal with TAB key
        		}
        	});
        	
       
            this.field.textProperty().addListener((observable, oldValue, newValue) -> {
            	var screenPos = anchor.localToScreen(0.0,anchor.getHeight());
            	this.validationPopup.show(this.field.getScene().getWindow(), screenPos.getX(), screenPos.getY() );
        	});
        	
    	}
    	
    	return this.validationPopup;
    }
}
