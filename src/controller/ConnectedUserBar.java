package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import model.RegisteredUser;
import javafx.fxml.FXMLLoader;


import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class ConnectedUserBar extends HBox {
	// property will be unidirectionally bound to currently logged in user property in app
	private ObjectProperty<RegisteredUser> userProperty = new SimpleObjectProperty<>();
	@FXML Label connectedUserBarDescription;
	@FXML Label connectedUserBarUser;
	@FXML Button connectedUserBarButton;
	
	public ConnectedUserBar() {
		this.userProperty.addListener( (e,oldUser,newUser)-> {
			this.getStyleClass().remove("notConnected");
			if (newUser == null ) {
				this.connectedUserBarDescription.setText("Vous n'êtes pas connecté.");
				this.getStyleClass().add("notConnected");
				this.connectedUserBarButton.setText("Connection");
			} else {
				this.connectedUserBarDescription.setText("Utilisateur connecté: ");
				this.connectedUserBarUser.setText(newUser.getUsername());
				this.connectedUserBarButton.setText("Déconnection");
			}
		} );
		
        var loader = new FXMLLoader(getClass().getResource("ConnectedUserBar.fxml"));	
        loader.setRoot(this);																	
        loader.setController(this); // register as fxml root controller 
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}

	public final ObjectProperty<RegisteredUser> getUserProperty(){
		return this.userProperty;
	}
	
	public Button getButton() {
		return this.connectedUserBarButton;
	}
}
