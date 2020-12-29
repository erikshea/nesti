package controller;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;


public class UserAccountLoginForm extends GridPane{
	public void initialize() {

	}

	public UserAccountLoginForm(){
        var loader = new FXMLLoader(getClass().getResource("UserAccountLoginForm.fxml"));	
        loader.setRoot(this);																	
        loader.setController(this); // register as fxml root controller 
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
}
