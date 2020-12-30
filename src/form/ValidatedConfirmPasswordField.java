package form;


public class ValidatedConfirmPasswordField  extends ValidatedBasePasswordField{
	private String matchingPasswordFieldSelector;
	private ValidatedBasePasswordField matchingPasswordField;
	
    public ValidatedConfirmPasswordField() {
		super();
		this.addValidator(
    		(val) -> val.equals(this.getMatchingPasswordField().getText()),
			"Correspond."
    		);
	}
    

    public void setFieldToMatch(String fieldSelector) {
    	this.matchingPasswordFieldSelector = fieldSelector;
    }
    
    public String getFieldToMatch() {
    	return this.matchingPasswordFieldSelector;
    }
    
    protected ValidatedBasePasswordField getMatchingPasswordField() {
    	if (this.matchingPasswordField == null) {
    		this.matchingPasswordField = (ValidatedBasePasswordField) this.getForm().lookup(matchingPasswordFieldSelector);
    	}
    	
    	return this.matchingPasswordField;
    }
}
