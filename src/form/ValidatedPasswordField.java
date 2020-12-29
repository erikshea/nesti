package form;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;


import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

public class ValidatedPasswordField  extends ValidatedField{
	private int minimumPasswordStrength = 80;

    public ValidatedPasswordField() {
		super();
		this.getChildren().remove(this.field);
		this.field = new PasswordField();
		this.getChildren().add(this.field);
		
		var strengthBar = new ProgressBar();
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
			(val) -> ( passwordStrength(val) >= this.minimumPasswordStrength ),
			"Suffisamment fort.");
	}

    
    private static double passwordStrength(String password) {
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
