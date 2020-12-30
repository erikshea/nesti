package form;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class BaseField  extends VBox{
	public Label label;
	public TextField field;
	
    public String getLabelText() {
    	return this.label.getText();
    }
    
    public void setLabelText(String text) {
    	this.label.setText(text);
    }
    
    public String getText() {
    	return this.field.getText();
    }
    
    public void setText(String text) {
    	this.field.setText(text);
    }
    
	public BaseField(){
    	this.label = new Label();
		this.field = new TextField();
    	this.getChildren().addAll(this.label, this.field);
	}
}
