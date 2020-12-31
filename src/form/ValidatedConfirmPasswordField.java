package form;

import javafx.application.Platform;

public class ValidatedConfirmPasswordField  extends ValidatedBasePasswordField{
	private String matchingPasswordFieldSelector;
	private ValidatedBasePasswordField matchingPasswordField;
	
    public ValidatedConfirmPasswordField() {
		super();
		this.addValidator(
    		(val) -> this.getMatchingPasswordField() != null && val.equals(this.getMatchingPasswordField().getText()),
			"Correspond."
    		);
	}

    public void setFieldToMatch(String fieldSelector) {
    	this.matchingPasswordFieldSelector = fieldSelector;
    	Platform.runLater( ()-> {
			this.getMatchingPasswordField().textProperty().addListener(e->this.applyValidators());
    		this.applyValidators();
    	});
    }
    
    public String getFieldToMatch() {
    	return this.matchingPasswordFieldSelector;
    }
    
    protected ValidatedBasePasswordField getMatchingPasswordField() {
    	if (this.matchingPasswordField == null && this.getForm() != null) {
    		this.matchingPasswordField = (ValidatedBasePasswordField) this.getForm().lookup(matchingPasswordFieldSelector);
    	}
    	
    	return this.matchingPasswordField;
    }
}
