package com.erikshea.nestiuseraccount.form;

import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;


/**
 * Base field inherited by all validated fields
 *
 */
public class BaseField  extends VBox{
	@FXML protected Label label;
	@FXML protected  TextField field;
	public final static int MAX_TEXT_LENGTH = 200;
	
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
    
    public void initialize() {
    	// to enforce max field length we use a TextFormatter (not a Text change listener
    	// because we don't want to modify a value inside its own listener).
    	field.setTextFormatter(new TextFormatter<>(
			c -> {
	    	    if (c.isContentChange()) {
	    	        if (c.getControlNewText().length() > MAX_TEXT_LENGTH) {
	    	            c.setText(c.getControlNewText().substring(0, MAX_TEXT_LENGTH));
	    	        }
	    	    }
	    	    return c;
	    	}
    	));
    	
    	this.needsLayoutProperty().addListener((e)->{ // Need to wait for layout phase to have access to fx:id
      		if (this.getId() != null) { 
      			this.getField().setId(this.getId()+"Input"); // set unique id for input field. Used with TestFX.
        	}
    	});
    }
    
	/**
	 * Adds TextField and Label
	 */
	public BaseField(){
		loadFxml();
	}

	protected void loadFxml() {
        var loader = new FXMLLoader(getClass().getResource("BaseField.fxml"));	
        loader.setRoot(this);																	
        loader.setController(this); // register as fxml root controller 
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	public StringProperty textProperty() {
		return this.getField().textProperty();
	}
	
	
	public TextField getField() {
		return this.field;
	}

}
