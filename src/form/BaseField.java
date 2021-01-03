package form;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


/**
 * Base field inherited by all validated fields
 *
 */
public class BaseField  extends VBox{
	public Label label;
	protected  TextField field;
	
    public String getLabelText() {
    	return this.label.getText();
    }
    
    public void setLabelText(String text) {
    	this.label.setText(text);
    }
    
    public String getText() {
    	return this.textProperty().get();
    }
    
    public void setText(String text) {
    	this.textProperty().set(text);
    }
    
	/**
	 * Adds TextField and Label
	 */
	public BaseField(){
    	this.label = new Label();
    	this.field = new TextField();
    	this.getChildren().add(this.label);
    	this.setField(new TextField());
    	
      	this.needsLayoutProperty().addListener((e)->{
      		if (this.getId() != null) { // On layout, set unique id for inner field. Used with TestFX.
      			this.getField().setId(this.getId()+"Input");
        	}
      	});

	}
	
	public StringProperty textProperty() {
		return this.getField().textProperty();
	}
	
	/**
	 * Sets the editable field. 
	 * @param field
	 */
	public void setField(TextField field) {
		if (this.getField() != null) { // if we're replacing an existing field (eg by a password field), remove old one
			this.getChildren().remove(this.getField());
		}
		this.field=field;
		this.getChildren().add(this.getField());
	}
	
	public TextField getField() {
		return this.field;
	}
}
