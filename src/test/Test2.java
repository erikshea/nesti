package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import application.NestiUserAccountMain;
import controller.MainWindowControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

class Test2 extends ApplicationTest  {
	@Override
	public void start (Stage stage) throws Exception {
	  Parent mainNode = new VBox(); 
	  stage.setScene(new Scene(mainNode));
	  stage.show();
	  stage.toFront();
	}
	
	@Test
	void test() {
		fail("Not yet implemented");
	}
}

	
	
