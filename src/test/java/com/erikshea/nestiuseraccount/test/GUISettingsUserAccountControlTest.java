package com.erikshea.nestiuseraccount.test;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import com.erikshea.nestiuseraccount.application.DatabaseManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import com.erikshea.nestiuseraccount.application.*;
import com.erikshea.nestiuseraccount.model.*;
import com.erikshea.nestiuseraccount.controller.*;

public class GUISettingsUserAccountControlTest extends ApplicationTest {
    private UserAccountControl control;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) {
        control = new UserAccountControl();
        var scene = new Scene(control);
		scene.getStylesheets().add(ApplicationSettings.class.getResource("UserAccount.css").toExternalForm()); // Attach CSS file to scene
        stage.setScene(scene);
        stage.show();
    }

    @Before
    public void setUp () throws Exception {
    	System.out.println("GUI test running. Please do not use your mouse or keyboard.");
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
    	
    	clickOn("Valider");
    	
    	clickOn("OK"); // Error because folder doesn't exist

    	lookup("#settingsAddress").queryTextInputControl().clear();
    	clickOn("#settingsAddress");
    	write("");
    	
    	clickOn("Valider");
    	
    	var databaseFile = new File("test_database.sqlite.db");
    	assertTrue(databaseFile.isFile()); // Make sure db file was created
    	
    	clickOn("Base de données");
    	clickOn("Ré-initialiser avec des données");
    	
    	// Check that user's info was entered correctly in the database
    	assertNotNull(RegisteredUserDAO.find("username", "judy"));
    	
    	// disconnect and delete test database file
    	try {
			DatabaseManager.disconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	databaseFile.delete();
    	
    	// Reset database to defaults, and repopulate.
    	clickOn("Base de données");
    	clickOn("Réglages"); 
    	clickOn("Valeurs par défaut");
    	clickOn("Valider"); 
    	clickOn("Base de données");
    	clickOn("Ré-initialiser avec des données");
    }
   
}