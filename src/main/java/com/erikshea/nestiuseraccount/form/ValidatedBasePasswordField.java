package com.erikshea.nestiuseraccount.form;

import java.io.IOException;

import javafx.fxml.FXMLLoader;


/**
 * Base field inherited by other password fields. Contains a PasswordField instead of a TextField
 */
public class ValidatedBasePasswordField  extends ValidatedField{
    @Override
	protected void loadFxml() {
        var loader = new FXMLLoader(getClass().getResource("ValidatedBasePasswordField.fxml"));	
        loader.setRoot(this);																	
        loader.setController(this); // register as fxml root controller 
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
}
