package com.erikshea.nestiuseraccount.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Account info bar below menus
 */
public class ConnectedUserBar extends HBox {
 	@FXML Button connectedUserBarButton;
	@FXML Label connectedUserBarUser;
	private UserAccountControl mainController;

    public void setMainController(UserAccountControl c) {
    	this.mainController = c;
    	
    	// listener for currently logged in user
		this.mainController.getLoggedInUserProperty().addListener( (e,oldUser,newUser)-> {
			this.getStyleClass().remove("connected"); // remove style class to prevent adding multiple times
			if (newUser == null ) { // If no user logged in
				this.connectedUserBarUser.setText("Vous n'êtes pas connecté."); 
			} else {
				this.getStyleClass().add("connected");
				this.connectedUserBarUser.setText(newUser.getUsername()); 
			}
			// Hide button and remove it from layout if no user logged in
			this.connectedUserBarButton.setVisible(newUser != null);
			this.connectedUserBarButton.setManaged(newUser != null);
		} );
    }
    
	public ConnectedUserBar() {
        var loader = new FXMLLoader(getClass().getResource("ConnectedUserBar.fxml"));	
        loader.setRoot(this);																	
        loader.setController(this); // register as fxml root controller 
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	@FXML protected void buttonAction(ActionEvent e) {
		this.mainController.logOutUser();
	}
}
