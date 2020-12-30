/*
____________________________________________________________________ 
FILE NAME : ImitamagochiMain.java
FILE LOCATION : /imitamagochi/src/
DESCRIPTION : JavaFX demo: Animals age and die, and can perform actions.
AUTHOR : Erik Shea <hoopsnake@gmail.com>
CREATION DATE : 2/10/2020
_____________________________________________________________________
*/


package application;


import controller.UserAccountControl;
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
			var scene = new Scene(mainWindow);	// Set window size, with main window controller as root
			// Context menus exist outside of node tree, so their styling done in scene
			scene.getStylesheets().add(this.getClass().getResource("UserAccount.css").toExternalForm());
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
