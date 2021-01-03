package form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

/**
 *	Base class for other validated fields. Sets up validator popup, and validator logic.
 */
public class ValidatedField  extends BaseField{
	public Popup validationPopup; // Popup containing all validator messages
	public Region validationPopupAnchor; // Region under which validator popup appears. defaults to field .
	protected String disableButtonSelector;	// Selector for button that must be disabled if field isn't valid
	protected Region buttonToDisable;	// Button to disable
	protected Region form;	// Form in which this field is contained
	
	private List<Predicate<String>> specialCases = new ArrayList<>();	// Special cases: field is valid if one checks regardless of other validators
	private Map<FieldValidator, Label> validators = new HashMap<>();	// Validators to check against field value

	public ValidatedField(){
		super();
		this.getStyleClass().add("requiredField");
		this.validationPopup = new Popup();
		this.validationPopup.setAutoHide(true); // hide on any key/mouse event
		this.validationPopup.setConsumeAutoHidingEvents(false); // hiding key/mouse event can also focus another field or manipulate window

		this.setOnMouseExited(e->this.validationPopup.hide());	// Hide validation messages on mouse exited

		var messagesBox = new VBox(); // Region inside popup that contains all messages.
		messagesBox.setMouseTransparent(true);
		
    	messagesBox.getStyleClass().add("validationMessages");
    	this.validationPopup.getContent().add(messagesBox);
    	
    	this.needsLayoutProperty().addListener((e)->this.applyValidators()); // Apply validators when assembling layout
	}

	/**
	 * Query form containing this field
	 * @return form region
	 */
	public Region getForm() {
		if (this.form == null && this.getScene() != null) { // If form parameter not set, query it
			this.form = (Region) this.getScene().lookup(".validatedForm");
		}
		return this.form;
	}

	
    /**
     * Add validator to field
     * @param validator function to validate field content against
     * @param message describing validation condition
     */
    public void addValidator(Predicate<String> validator, String message) {
    	var popupVBox = (VBox) this.validationPopup.getContent().get(0); // Get region containing validator labels
    	
        var validatorLabel = new Label(message);
        popupVBox.getChildren().add(validatorLabel); // Add new label
        
        this.validators.put(new FieldValidator(message, validator), validatorLabel); // store new label in hashmap, with validator as key
    }
    
    /**
     * Show popup containing validator labels
     */
    protected void showValidationPopup() {
    	if ( this.getScene() != null ) {
        	var anchor = this.validationPopupAnchor==null?this.getField():this.validationPopupAnchor; // Region under which to show popup
        	var position = anchor.localToScreen(0.0,anchor.getHeight()); // translate anchor position to screen coordinates

    		this.validationPopup.show(this.getScene().getWindow(), position.getX(), position.getY() ); // Show popup 
    	}
    }
    
    
    /**
     * Add special case: if it's satisfied, field is always valid regardless of other validators
     * @param validator
     */
    public void addSpecialCase(Predicate<String> validator) {
    	this.specialCases.add(validator);
    	this.applyValidators(); // Refresh validators in case special case applies
    }	
    
    /**
     * Check field value all special cases
     * @return true if one of the special cases is valid
     */
    private boolean specialCasesValidate() {
    	boolean result = false;
    	
    	for ( var v:this.specialCases) {
    		if (v.test(this.getText())) {
    			result = true;
    		}
    	}
    	
    	return result;
    }
    
    /**
     *	Set field text
     */
    @Override
    public void setText(String text) {
    	super.setText(text);
    	this.validationPopup.hide(); // Hide validation popup that appeared when field text change event fired
    }
    
    /**
     * Re-apply validators by changing value to random characters, and changing it back
     */
    public void applyValidators() {
    	var temp = this.getText();
    	this.setText("EcujwEGh3siX3EqRNeO48V5j1TNlbdYCzJGewQioUFCEcujwEGh3siX3EqRNeO48V5j1TNlbdYCzJGewQioUFCEcujwEGh3siX3EqRNeO48V5j1TNlbdYCzJGewQioUFCEcujwEGh3siX3EqRNeO48V5j1TNlbdYCzJGewQioUFC");
    	this.setText(temp);
    }
    
    /**
     * 	Get button that must be disabled if field isn't valid
     * @return
     */
    public Region getButtonToDisable() {
    	if (this.buttonToDisable == null && this.getForm() != null) { // Look it up if it doesn't exist
    		this.buttonToDisable = (Region) this.getForm().lookup(this.disableButtonSelector);
    	}
    	return this.buttonToDisable;
    }
    
    /**
     *	Sets field (subclass of TextField) contained in this region
     */
    @Override
    public void setField(TextField field){
    	super.setField(field);
    	
		this.setUpFieldListeners(); // If field changes, listeners must be re-established
    }
    
	/**
	 * Logic executed when field or its content changes
	 */
	private void setUpFieldListeners(){
		this.getField().textProperty().addListener((obs, oldText, newText)->{ // Field text listener
			var isValid = new SimpleBooleanProperty(true); // Need mutable var to be able to modify in forEach
			this.validators.forEach( (validator, label) -> { // Loop through all validators
				label.getStyleClass().remove("valid");// Remove class so that multiple copies are not added
				if ( validator.validate(newText)) {	
					label.getStyleClass().add("valid");	// validator label marked valid if validator pases
				} else if (!this.specialCasesValidate()){ // if any validator fails, and special cases don't validate
					isValid.set(false); // Field is invalid
				}
			});
			
			this.getStyleClass().remove("valid");
			if (isValid.get()) {
				this.getStyleClass().add("valid");
			}

			if (this.getButtonToDisable() != null){
				this.getButtonToDisable().setDisable(						   // Button disabled if:
					   this.getForm().lookupAll(".requiredField.valid").size() // number of valid required fields
					!= this.getForm().lookupAll(".requiredField").size() );	   // different from number of required fields
			}
			
			this.showValidationPopup();
		});

		// When field gains focus
        this.getField().focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue) {
    			this.showValidationPopup(); // Show popup
    		} else {
    			this.validationPopup.hide(); // Must specify because auto-hide won't trigger on field traversal with TAB key
    		}
    	});
	}
	
    
    public void setDisableButton(String buttonSelector) {
    	this.disableButtonSelector = buttonSelector;
    }
    
    public String getDisableButton() {
    	return this.disableButtonSelector;
    }
}
