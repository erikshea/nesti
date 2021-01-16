/*
____________________________________________________________________ 
FILE NAME : NestiUserAccountMain.java
FILE LOCATION : /nesti/src/
DESCRIPTION : Basic user account manager. 
AUTHOR : Erik Shea <hoopsnake@gmail.com>
CREATION DATE : 26/12/2020
_____________________________________________________________________
*/


package com.erikshea.nestiuseraccount.application;


import com.erikshea.nestiuseraccount.controller.UserAccountControl;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/*
 * sets scene, loads main window controller
 * */
public class NestiUserAccountMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			var mainWindow = new UserAccountControl(); // Main window is handled by its own controller
			var scene = new Scene(mainWindow);	// Set main window controller as scene root
			
			scene.getStylesheets().add(ApplicationSettings.class.getResource("UserAccount.css").toExternalForm()); // Attach CSS file to scene
			primaryStage.setScene(scene);
			primaryStage.setTitle("Nesti Account Manager");
			primaryStage.setResizable(false); 
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	public static void main(String[] args) {
		launch(args);
	}
}
