package controller;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;


public class UserAccountShowInfo extends GridPane{
	public void initialize() {

	}

	public UserAccountShowInfo(){
        var loader = new FXMLLoader(getClass().getResource("UserAccountShowInfo.fxml"));	
        loader.setRoot(this);																	
        loader.setController(this); // register as fxml root controller 
        	
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
}
