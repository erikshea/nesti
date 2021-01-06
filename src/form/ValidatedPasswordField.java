package form;

import java.util.List;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 *	Validated password field checks password and shows a strength indicator bar below field
 */
public class ValidatedPasswordField  extends ValidatedBasePasswordField{
	private int minimumPasswordStrength = 80; // Minimum calculated strength for field to validate
	ProgressBar strengthBar;
	
	@Override
	public void initialize() {
		super.initialize();
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
	
    
    protected static double passwordStrength(String password) {
    	var possibleChars = 0; // set of potentially different characters in password
    	
    	for ( var checkRange:List.of("09", "az", "AZ", " /") ) { 
    		if (password.matches("^.*[" + checkRange.charAt(0) + "-" + checkRange.charAt(1) + "].*$")) { // If any character is within those ranges
    			possibleChars += checkRange.charAt(1) - checkRange.charAt(0) + 1; // add distance between the chars
    		}
    	}
    	
        // Equation source: https://www.ssi.gouv.fr/administration/precautions-elementaires/calculer-la-force-dun-mot-de-passe/
        return password.length() *  Math.log(possibleChars)/Math.log(2);
    }
}
