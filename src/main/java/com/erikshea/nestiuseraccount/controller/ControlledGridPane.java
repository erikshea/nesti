package com.erikshea.nestiuseraccount.controller;

import javafx.scene.layout.GridPane;

/**
 *	Base class for all regions controlled by our main controller
 */
public class  ControlledGridPane extends GridPane{
	protected UserAccountControl mainController; // Reference to main controller
	
    public void setMainController(UserAccountControl c) {
    	this.mainController = c;
    }
    
    public UserAccountControl getMainController() {
    	return this.mainController;
    }
}

