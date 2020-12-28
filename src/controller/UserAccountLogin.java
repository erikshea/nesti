package controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXML;

public class UserAccountLogin extends GridPane{
    private MainWindowControl mainController;	// for access to other controllers

    
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
