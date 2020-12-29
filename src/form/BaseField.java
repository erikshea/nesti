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



public class BaseField  extends VBox{
	public Label label;
	public TextField field;
	
    public String getLabelText() {
    	return this.label.getText();
    }
    
    public void setLabelText(String text) {
    	this.label.setText(text);
    }
    
	public BaseField(){
    	this.label = new Label();
		this.field = new TextField();
    	this.getChildren().addAll(this.label, this.field);
	}
}
