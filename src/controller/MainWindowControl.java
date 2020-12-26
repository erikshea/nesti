package controller;
import java.io.IOException;

import org.controlsfx.control.PopOver;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 * Sets up main window and spawns menu, animal regions..
 * acts as a bridge between different controllers
 */
public class MainWindowControl extends BorderPane{
	@FXML private MenuBarControl mainMenuBar;		// Top menu
	@FXML private UserAccountRegister registerForm;
	
	/*
	 * set up game elements (those not in the .fxml) 
	 * */
	public void initialize() {
		this.mainMenuBar.setMainController(this);	// Pass controllers a reference to current instance, to act 
													// as an intermediary. TODO: decouple controllers with mediator
    	/*ContextMenu usernameValidator = new ContextMenu();
        usernameValidator.setAutoHide(false);
        usernameValidator.getItems().add(
                new MenuItem("Please enter username"));
        usernameValidator.show(this.mainMenuBar, Side.RIGHT, 10, 0);*/
	}


	
	public MainWindowControl(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindowControl.fxml"));	// load .fxml 
        loader.setRoot(this);																	// as root
        loader.setController(this); // register as its controller 
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}

}
