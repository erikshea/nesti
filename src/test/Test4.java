package test;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import controller.ConnectedUserBar;
import controller.UserAccountControl;
import controller.UserAccountRegisterForm;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class Test4 extends ApplicationTest {
    private UserAccountControl control;
    private Node registerAccountMenu, connectMenu, accountInfoMenu;
    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) {
        control = new UserAccountControl();
        //button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(control));
        stage.show();
    }

    @Before
    public void setUp () throws Exception {

    	
    }

    @After
    public void tearDown () throws Exception {
      FxToolkit.hideStage();
      release(new KeyCode[]{});
      release(new MouseButton[]{});
    }
    
  

    @Test
    public void email_field_validation() {
    	System.out.println(control.lookup("UserAccountInfo"));
    	System.out.println(control.lookup("UserAccountLoginForm"));
    	
    	//FxRobot.clickOn
    	/*Platform.runLater(()->{ 

        	System.out.println(this.connectedUserBar);
    		
    		
    		// runLater needed to avoid IllegalStateException when running unit tests without FXRobot
	    	fieldEmail.textProperty().set("test@test.com");
	    	fieldPassword.textProperty().set("test@test.com");
	    	
	    	
	    	assertTrue(fieldEmail.getStyleClass().contains("valid"));
    	});*/
    }

    public TextField getField(String id) {
    	return (TextField)control.lookup("#" + id);
    }
}