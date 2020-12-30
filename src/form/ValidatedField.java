package form;

import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;



public class ValidatedField  extends BaseField{
	public Popup validationPopup = null;
	public Region validationPopupAnchor = null;
	protected String disableButtonSelector = null;
	protected Region form;
	
	public interface FieldValidator {
		  boolean validate(String fieldValue);
	}
    
    public void setDisableButton(String buttonSelector) {
    	this.disableButtonSelector = buttonSelector;
    }
    
    public String getDisableButton() {
    	return this.disableButtonSelector;
    }
	
	public ValidatedField(){
		super();
		this.getStyleClass().add("requiredField");
	}

	public Region getForm() {
		if (this.form == null) {
			this.form = (Region) this.getScene().lookup(".validatedForm");
		}
		return this.form;
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
            
            this.getStyleClass().remove("valid");
            if (popupContent.lookupAll(".validationMessages Label.valid").size() == popupContent.lookupAll(".validationMessages Label").size())
            {
            	this.getStyleClass().add("valid");
            }
            
            Region buttonToDisable = (Region) this.getForm().lookup(this.disableButtonSelector);
            
            if (buttonToDisable != null) {
            	buttonToDisable.setDisable(    this.getForm().lookupAll(".requiredField.valid").size()
            								!= this.getForm().lookupAll(".requiredField").size() );
            }
    	}); 
    }
    
    protected Popup getValidationPopup() {
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
                	this.validationPopup.show(this.getScene().getWindow(), screenPos.getX(), screenPos.getY() );
        		} else {
        			this.validationPopup.hide(); // Must specify because auto-hide won't trigger on field traversal with TAB key
        		}
        	});
        	
            
            this.field.textProperty().addListener((observable, oldValue, newValue) -> {
            	var screenPos = anchor.localToScreen(0.0,anchor.getHeight());
            	this.validationPopup.show(this.getScene().getWindow(), screenPos.getX(), screenPos.getY() );
        	});
        	
    	}
    	
    	return this.validationPopup;
    }
    
    
    public void addSpecialCase(FieldValidator validator) {
    	this.field.textProperty().addListener((observable, oldValue, newValue) -> {
    		this.getStyleClass().remove("valid");;
            if(validator.validate(newValue)){
            	this.getStyleClass().add("valid");
            }
    	}); 
    	applyValidators();
    }
    
    @Override
    public void setText(String text) {
    	super.setText(text);
    	this.validationPopup.hide();
    }
    
    public void applyValidators() {
    	var temp = this.field.getText();
    	this.setText("EcujwEGh3siX3EqRNeO48V5j1TNlbdYCzJGewQioUFC");
    	this.setText(temp);
    }
    
}
