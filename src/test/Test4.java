package test;
import  org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

import controller.UserAccountRegister;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Test4 extends ApplicationTest {
    private TextField fieldUsername, fieldEmail, fieldFirstName, fieldName, fieldCity;
    private PasswordField fieldPassword, fieldConfirmPassword;
    private UserAccountRegister form;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) {
        form = new UserAccountRegister();
        //button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(form));
        stage.show();
    }

    @Before
    public void setUp () throws Exception {
    	this.fieldUsername = (TextField)form.lookup("#fieldUsername");
    	this.fieldEmail = (TextField)form.lookup("#fieldEmail");
    	this.fieldFirstName = (TextField)form.lookup("#fieldFirstName");
    	this.fieldName = (TextField)form.lookup("#fieldName");
    	this.fieldCity = (TextField)form.lookup("#fieldCity");
    	this.fieldPassword = (PasswordField)form.lookup("#fieldPassword");
    	this.fieldConfirmPassword = (PasswordField)form.lookup("#fieldConfirmPassword");
    }

    @After
    public void tearDown () throws Exception {
      FxToolkit.hideStage();
      release(new KeyCode[]{});
      release(new MouseButton[]{});
    }
    
  

    @Test
    public void email_field_validation() {
    	fieldEmail.textProperty().set("test@test.com");
    	fieldPassword.textProperty().set("test@test.com");
    	
    	
    	assertTrue(fieldEmail.getStyleClass().contains("valid"));
    	/*Button submitButton = lookup("#submitButton").query();

    	
    	
    	clickOn("#fieldEmail");
    	write("test@test.com");*/
    	
    }


}