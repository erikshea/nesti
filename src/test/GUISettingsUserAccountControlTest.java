package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import model.*;
import controller.*;

public class GUISettingsUserAccountControlTest extends ApplicationTest {
    private UserAccountControl control;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) {
        control = new UserAccountControl();
        var scene = new Scene(control);
		scene.getStylesheets().add(this.getClass().getResource("/application/UserAccount.css").toExternalForm()); // Attach CSS file to scene
        stage.setScene(scene);
        stage.show();
    }

    @Before
    public void setUp () throws Exception {
    	System.out.println("GUI test running. Please do not use your mouse or keyboard.");
    	clickOn("Base de données");
    	clickOn("Ré-initialiser avec des données"); // reset database with test entries
    	
    	clickOn("#connectedUserBarButton"); // click on log out button
    	
    }
    

    @After
    public void tearDown () throws Exception {
    	System.out.println("GUI test done.");
		FxToolkit.hideStage();
		release(new KeyCode[]{});
		release(new MouseButton[]{});
    }
    


    
    @Test
    public void GUI_test_settings_page() {
    	clickOn("Base de données");
    	clickOn("Réglages"); // Click on information button
    	
    	
    	clickOn("#databaseType");
    	clickOn("sqlite");
    	

    	lookup("#settingsAddress").queryTextInputControl().clear();
    	clickOn("#settingsAddress");
    	write("./assets/folder_that_doesnt_exist");

    	lookup("#settingsDatabaseName").queryTextInputControl().clear();
    	clickOn("#settingsDatabaseName");
    	write("test_database");
    	
    	lookup("#settingsLogin").queryTextInputControl().clear();
    	clickOn("#settingsLogin");
    	write("testuser");
    	
    	lookup("#settingsPassword").queryTextInputControl().clear();
    	clickOn("#settingsPassword");
    	write("testpassword");
    	
    	clickOn("Enregistrer");
    	
    	clickOn("OK"); // Error because folder doesn't exist

    	lookup("#settingsAddress").queryTextInputControl().clear();
    	clickOn("#settingsAddress");
    	write("./assets/database");
    	
    	clickOn("Enregistrer");

    	clickOn("Base de données");
    	clickOn("Ré-initialiser avec des données");
		//System.out.println(new File("./assets/test_folder/test_database.sqlite.db").isFile());;

    	
    	Platform.runLater(()->{ // Check that user's info was entered correctly in the database
	   		var userTest = RegisteredUserDAO.find("username", "judy");
	   		assertNotNull(userTest);
    	});
    }
   
}