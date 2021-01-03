package controller;

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
	@FXML Label connectedUserBarDescription;
	@FXML Label connectedUserBarUser;
	private UserAccountControl mainController;

    public void setMainController(UserAccountControl c) {
    	this.mainController = c;
    	
    	// listener for currently logged in user
		this.mainController.getLoggedInUserProperty().addListener( (e,oldUser,newUser)-> {
			this.getStyleClass().remove("notConnected"); // remove style class to prevent adding multiple times
			if (newUser == null ) { // If no user logged in
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
