package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class ConnectedUserBar extends HBox {
	private UserAccountControl mainController;
	@FXML Label connectedUserBarDescription;
	@FXML Label connectedUserBarUser;
	@FXML Button connectedUserBarButton;
	
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
	
	public void setMainController(UserAccountControl mainController) {
		this.mainController = mainController;
		this.mainController.getloggedInUserProperty().addListener( (e,oldUser,newUser)-> {
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
	}
	
	@FXML protected void buttonAction(ActionEvent e) {
		this.mainController.disconnectUser();
	}
	
}
