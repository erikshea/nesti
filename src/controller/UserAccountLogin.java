package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class UserAccountLogin extends GridPane{
    private MainWindowControl mainController;	// for access to other controllers
    @FXML private Text actiontarget;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }
    
    /**
     * Loads .fxml as root
     */
    public UserAccountLogin()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("userAccountRegister.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    

	/**
	 * Sets a reference to the main controller (for console, and to communicate with other controllers)
	 * @param c main window controller
	 */
	void setMainController(MainWindowControl c)
	{
		this.mainController = c;
	}

	
}
