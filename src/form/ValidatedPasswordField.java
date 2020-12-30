package form;

import java.util.List;
import javafx.scene.control.ProgressBar;

public class ValidatedPasswordField  extends ValidatedBasePasswordField{
	private int minimumPasswordStrength = 80;

    public ValidatedPasswordField() {
		super();
		var strengthBar = new ProgressBar(0);
		this.field.textProperty().addListener((observable, oldValue, newValue) -> {
    		var progress = passwordStrength(newValue)/(2*this.minimumPasswordStrength);
    		strengthBar.setProgress(progress);
    		strengthBar.getStyleClass().removeAll("great", "good", "poor", "very-poor");
    		if(progress > 0.75) {
    			strengthBar.getStyleClass().add("great");
    		} else if (progress > 0.5) {
    			strengthBar.getStyleClass().add("good");
    		}else if (progress > 0.25){
    			strengthBar.getStyleClass().add("poor");
    		}else {
    			strengthBar.getStyleClass().add("very-poor");
    		}
    	});
		this.getChildren().add(strengthBar);
		this.validationPopupAnchor = strengthBar;
		
    	this.addValidator(
			(val) -> passwordStrength(val) >= this.minimumPasswordStrength,
			"Suffisamment fort."
		);
    	this.addValidator(
			(val) -> val.matches("^.*[0-9].*$"),
			"Contient au moins un chiffre."
		);
    	this.addValidator(
			(val) -> val.matches("^.*[a-zA-Z].*$"),
			"Contient au moins une lettre."
		);
	}

    
    protected static double passwordStrength(String password) {
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
	
}
