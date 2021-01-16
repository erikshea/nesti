package com.erikshea.nestiuseraccount.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import com.erikshea.nestiuseraccount.model.*;
import com.erikshea.nestiuseraccount.controller.*;
import com.erikshea.nestiuseraccount.application.*;

public class GUIUserAccountControlTest extends ApplicationTest {
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
    	clickOn("Base de données");
    	clickOn("Ré-initialiser avec des données"); // reset database with test entries
    	
    }
    

    @After
    public void tearDown () throws Exception {
    	System.out.println("GUI test done.");
		FxToolkit.hideStage();
		release(new KeyCode[]{});
		release(new MouseButton[]{});
    }
    

    @Test
    public void GUI_test_login_page() {
       	clickOn("#fieldConnectionIdentifierInput");
    	write("judy");
    	
    	clickOn("#connectButton"); // Try to connect with only login
    	assertNotNull(control.lookup("UserAccountLoginForm")); // Check that we didn't leave login form

    	clickOn("OK");	// Click "OK" to dismiss alert saying we can't connect
    	
       	clickOn("#fieldConnectionPasswordInput");
    	write("1ddddddddddddddddddddddddd" + "E");
    	
    	clickOn("#connectButton"); // Try to connect with wrong password (1 extra 'E')
    	assertNotNull(control.lookup("UserAccountLoginForm")); // Check that we didn't leave login form

    	clickOn("OK");	// Click "OK" to dismiss alert saying we can't connect

       	clickOn("#fieldConnectionPasswordInput");
    	push(KeyCode.END);
    	push(KeyCode.BACK_SPACE); // remove extra "E"


    	clickOn("#connectButton"); // Try to connect with correct password
    	
    	assertNull(control.lookup("UserAccountLoginForm")); // Check that we left the login form...
    	assertNotNull(control.lookup("UserAccountInfo")); // ...and are now in the information form
    	
    	// Check that judy is logged in.
    	Platform.runLater(()->assertEquals(control.getLoggedInUser().getUsername(),"judy") );
    }
    
    @Test
    public void GUI_test_login_page_with_email() {
       	clickOn("#fieldConnectionIdentifierInput");
    	write("dsq@d.dd"); // Enter judy's email as identifier
    	clickOn("#fieldConnectionPasswordInput");
       	write("1ddddddddddddddddddddddddd");
       	clickOn("#connectButton"); // Try to connect with judy's password
		assertNotNull(control.lookup("UserAccountInfo")); // Check we're in the information page
		
    	// Check that judy is logged in.
    	Platform.runLater(()->assertEquals(control.getLoggedInUser().getUsername(),"judy") );
    }
    
    @Test
    public void GUI_test_information_page() {
       	clickOn("#fieldConnectionIdentifierInput");
    	write("judy");
       	clickOn("#fieldConnectionPasswordInput");
    	write("1ddddddddddddddddddddddddd");
    	clickOn("#connectButton"); // log in
    	
    	
    	assertNotNull(control.lookup("UserAccountInfo"));
    	
    	
    	clickOn("#fieldModifyUsernameInput");
    	push(KeyCode.END);
    	push(KeyCode.BACK_SPACE);
    	write("e"); // Change username to "jude"

    	clickOn("#fieldModifyEmailInput");
    	push(KeyCode.END);
    	push(KeyCode.BACK_SPACE);
    	push(KeyCode.BACK_SPACE);
    	write("co.uk"); // Change email to "dsq@d.co.uk"
    	
    	
    	clickOn("#fieldModifyFirstNameInput");
    	write("Judith");
    	clickOn("#fieldModifyLastNameInput");
    	write("Filson");
    	clickOn("#fieldModifyCityInput");
    	write("Pawnee");
    	clickOn("#fieldOldPasswordInput");
    	write("1ddddddddddddddddddddddddd");
    	clickOn("#fieldModifyPasswordInput");
    	write("2eeeeeeeeeeeEEEEEEEEEEEEEE");
    	clickOn("#fieldConfirmModifyPasswordInput");
    	write("2eeeeeeeeeeeEEEEEEEEEEEEEE");
    	clickOn("#saveButton");
    	
    	Platform.runLater(()->{ // Check that user's info was entered correctly in the database
    		assertNotNull(control.lookup("UserAccountInfo")); // Check we're in the information page
    		assertNull(RegisteredUserDAO.find("username", "judy")); // check that old username doesn't exist
    		// check modified user info is in data source
    		var modifiedUser = RegisteredUserDAO.find("username", "jude");
    		assertNotNull(modifiedUser);
    		assertEquals(modifiedUser.getEmail(), "dsq@d.co.uk");
    		assertEquals(modifiedUser.getFirstName(), "Judith");
    		assertEquals(modifiedUser.getLastName(), "Filson");
    		assertEquals(modifiedUser.getCity(), "Pawnee");
    		assertTrue(modifiedUser.isPassword("2eeeeeeeeeeeEEEEEEEEEEEEEE"));
    	});
    }
    
    @Test
    public void GUI_test_registration_page() {
    	clickOn("#registerButton");
    	
    	clickOn("#fieldUsernameInput");
    	write("test_user");
    	clickOn("#fieldEmailInput");
    	write("tester@test.org");
    	clickOn("#fieldFirstNameInput");
    	write("Tester");
    	clickOn("#fieldLastNameInput");
    	write("Testington");
    	clickOn("#fieldCityInput");
    	write("Testville");
    	clickOn("#fieldPasswordInput");
    	write("132TestTestTest");
    	clickOn("#fieldConfirmPasswordInput");
    	write("132TestTestTest");
    	clickOn("#registerSubmitButton");
    	
    	Platform.runLater(()->{ // Check that user's info was entered correctly in the database
	   		assertNotNull(control.lookup("UserAccountInfo")); // Check we're in the information page
	   		var userTest = RegisteredUserDAO.find("username", "test_user");
	   		assertNotNull(userTest);
	   		assertEquals(userTest.getEmail(), "tester@test.org");
	   		assertEquals(userTest.getFirstName(), "Tester");
	   		assertEquals(userTest.getLastName(), "Testington");
	   		assertEquals(userTest.getCity(), "Testville");
	   		assertTrue(userTest.isPassword("132TestTestTest"));
    	});
    }
    

    
}