package form;

import java.util.List;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

/**
 *	Validated password field checks password and shows a strength indicator bar below field
 */
public class ValidatedPasswordField  extends ValidatedBasePasswordField{
	private int minimumPasswordStrength = 80; // Minimum calculated strength for field to validate
	ProgressBar strengthBar;
	
    public ValidatedPasswordField() {
		super();

		this.strengthBar = new ProgressBar(0); // Strength indicator bar
		this.getChildren().add(this.strengthBar); // Add at end of field region
		this.validationPopupAnchor = this.strengthBar; // Popup should appear below strength bar
		
    	this.addValidator(
			(val) -> passwordStrength(val) >= this.minimumPasswordStrength,
			"Suffisamment fort."
		);
    	this.addValidator(
			(val) -> val.matches("^.*[\\d].*$"), // At least one digit
			"Contient au moins un chiffre."
		);
    	this.addValidator(
			(val) -> val.matches("^.*[a-zA-Z].*$"), // At least one letter
			"Contient au moins une lettre."
		);
	}

    
    protected static double passwordStrength(String password) {
    	var possibleChars = 0; // Approximation of size set of potentially different characters in password
    	
    	for ( var checkRange:List.of("09", "az", "AZ") ) { // If any character is within those ranges
    		if (password.matches("^.*[" + checkRange.charAt(0) + "-" + checkRange.charAt(1) + "].*$")) { // Build checking regex
    			possibleChars += checkRange.charAt(1) - checkRange.charAt(0) + 1; // Disctance between the chars
    		}
    	}
    	
        if ( password.matches("^.*\\W.*$") ) { // special char
            possibleChars += 50;
        }
        // Equation source: https://www.ssi.gouv.fr/administration/precautions-elementaires/calculer-la-force-dun-mot-de-passe/
        return password.length() *  Math.log(possibleChars)/Math.log(2);
    }


	@Override
	public void setField(TextField field) {
		super.setField(field);
		// When field is changed, add field text listener to update strength bar
		this.textProperty().addListener((observable, oldText, newText) -> {
    		var progress = passwordStrength(newText)/(2*this.minimumPasswordStrength); // Halsway point of bar is minimum strength
    		this.strengthBar.setProgress(progress);
    		this.strengthBar.getStyleClass().removeAll("great", "good", "poor", "very-poor");
    		if(progress > 0.75) { // Add style class depending on strength relative to minimum strength
    			this.strengthBar.getStyleClass().add("great");
    		} else if (progress > 0.5) {
    			this.strengthBar.getStyleClass().add("good");
    		}else if (progress > 0.25){
    			this.strengthBar.getStyleClass().add("poor");
    		}else {
    			this.strengthBar.getStyleClass().add("very-poor");
    		}
    	});
	}
	
}
