package com.erikshea.nestiuseraccount.form;


/**
 *	Confirm password field checks if it matches against another password field
 */
public class ValidatedConfirmPasswordField  extends ValidatedBasePasswordField{
	private String matchingPasswordFieldSelector;
	private ValidatedBasePasswordField matchingPasswordField;
	
    public ValidatedConfirmPasswordField() {
		super();
		this.addValidator(
    		(val) -> val.equals(this.getMatchingPasswordField().getText()),	// contents match
			"Correspond."
    	);
	}

    protected ValidatedBasePasswordField getMatchingPasswordField() {
    	if (this.matchingPasswordField == null && this.getForm() != null) { // If not set, try to look up matching field
    		this.matchingPasswordField = (ValidatedBasePasswordField) this.getForm().lookup(matchingPasswordFieldSelector);
    	}
    	
    	return this.matchingPasswordField;
    }
    
    /**
     * Sets matching field from a CSS selector
     * @param fieldSelector CSS selector for matching field within form
     */
    public void setFieldToMatch(String fieldSelector) {
    	this.matchingPasswordFieldSelector = fieldSelector;

    	this.needsLayoutProperty().addListener((o)->{
			this.getMatchingPasswordField().textProperty().addListener(e->this.applyValidators());
    		this.applyValidators();
    	});
    }
    
    public String getFieldToMatch() {
    	return this.matchingPasswordFieldSelector;
    }
}
