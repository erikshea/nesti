package com.erikshea.nestiuseraccount.test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;


import org.junit.After;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import com.erikshea.nestiuseraccount.model.*;
import com.erikshea.nestiuseraccount.controller.*;
import com.erikshea.nestiuseraccount.application.*;

public class UserAccountControlTest extends ApplicationTest {
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

    @After
    public void tearDown () throws Exception {
		FxToolkit.hideStage();
    }
    
    @Test
    public void sql_injection() {
    	try {
			RegisteredUserDAO.populateTable(); // Re-initialize data source with test data
		} catch (SQLException e) {
			fail("Database connection error.");
		}
    	Platform.runLater(()->{
    		RegisteredUserDAO.find("username", "bob;DELETE FROM registered_user;");
    		assertNotNull(RegisteredUserDAO.find("username", "bob"));

    		RegisteredUserDAO.find("email", "bobby@bob.bob;DELETE FROM registered_user;");
    		assertNotNull(RegisteredUserDAO.find("username", "bob"));
    		
    		var user = new RegisteredUser();
    		
    		// create user with unvalidated information
    		user.setUsername("bobster");
    		user.setEmail("bobs@ter.com");
    		user.setFirstName("bob");
    		user.setLastName("bob");
    		user.setCity("bob");
    		user.setPasswordHash("bob");
    		user.setRegistrationDate("1-1-1;DELETE FROM registered_user;");
    		
    		try {
				RegisteredUserDAO.insert(user);
			} catch (SQLException e) {
				e.printStackTrace();
				fail("SQL error"); 
			}
    		
    		assertNotNull(RegisteredUserDAO.find("username", "bob"));
    	});

    }
    
    @Test
    public void registration_form_truncate_oversize_field_text() {
    	try {
			RegisteredUserDAO.populateTable(); // Re-initialize data source with test data
		} catch (SQLException e) {
			fail("Database connection error.");
		}
    	Platform.runLater(()->{
        	control.showRegion(new UserAccountRegisterForm());
        	var usernameInput = lookup("#fieldUsernameInput").queryTextInputControl();
        	usernameInput.setText("jusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddy");
        	assertTrue(usernameInput.getText().length() <= 200);
        	
        	var emailInput = lookup("#fieldEmailInput").queryTextInputControl();
        	emailInput.setText("jusdqsqdsqd@sqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddy.com");
          	assertTrue(emailInput.getText().length() <= 200);
          	
        	var firstNameInput = lookup("#fieldFirstNameInput").queryTextInputControl();
        	firstNameInput.setText("jusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddy.com");
          	assertTrue(firstNameInput.getText().length() <= 200);
          	
        	var lastNameInput = lookup("#fieldLastNameInput").queryTextInputControl();
        	lastNameInput.setText("jusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddy.com");
          	assertTrue(lastNameInput.getText().length() <= 200);
          	
        	var cityInput = lookup("#fieldCityInput").queryTextInputControl();
        	cityInput.setText("jusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddy.com");
          	assertTrue(cityInput.getText().length() <= 200);
          	
        	var passwordInput = lookup("#fieldPasswordInput").queryTextInputControl();
        	passwordInput.setText("jusdqsqdsqdsqdsdqsééé---sd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddy");
          	assertTrue(passwordInput.getText().length() <= 200);
          	
          	var confirmPasswordInput = lookup("#fieldConfirmPasswordInput").queryTextInputControl();
          	confirmPasswordInput.setText("jusdqsqdsqdsqdsdqséééé---sd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddyjusdqsqdsqdsqdsdqssd1111DFSDQFFsdsqssqddy");
          	assertTrue(confirmPasswordInput.getText().length() <= 200);
    	});
    }
   
    
    @Test
    public void registration_form_field_validation() {
    	try {
			RegisteredUserDAO.populateTable(); // Re-initialize data source with test data
		} catch (SQLException e) {
			fail("Database connection error.");
		}
    	Platform.runLater(()->{
        	control.showRegion(new UserAccountRegisterForm());
        	var usernameStyle = lookup("#fieldUsername").queryAs(Region.class).getStyleClass();
        	var usernameInput = lookup("#fieldUsernameInput").queryTextInputControl();
        	
        	usernameInput.setText("judy");
        	assertFalse(usernameStyle.contains("valid")); // taken
        	usernameInput.setText("");
        	assertFalse(usernameStyle.contains("valid")); // invalid
        	usernameInput.setText("b@b");
        	assertFalse(usernameStyle.contains("valid")); // invalid
        	usernameInput.setText("&bob");
        	assertFalse(usernameStyle.contains("valid")); // invalid
        	usernameInput.setText(";bob");
        	assertFalse(usernameStyle.contains("valid")); // invalid
        	usernameInput.setText("bo'bby");
        	assertFalse(usernameStyle.contains("valid")); // invalid
        	usernameInput.setText("_-Bobby-_");
        	assertTrue(usernameStyle.contains("valid"));
        	usernameInput.setText("1234");
        	assertTrue(usernameStyle.contains("valid"));
        	usernameInput.setText("bobby");
        	assertTrue(usernameStyle.contains("valid"));
        	

        	var emailStyle = lookup("#fieldEmail").queryAs(Region.class).getStyleClass();
        	var emailInput = lookup("#fieldEmailInput").queryTextInputControl();
        	
        	emailInput.setText("bob");
        	assertFalse(emailStyle.contains("valid")); // invalid
        	emailInput.setText("");
        	assertFalse(emailStyle.contains("valid")); // invalid
        	emailInput.setText("bob.com");
        	assertFalse(emailStyle.contains("valid")); // invalid
        	emailInput.setText("bob@.com");
        	assertFalse(emailStyle.contains("valid")); // invalid
        	emailInput.setText("@bob.com");
        	assertFalse(emailStyle.contains("valid")); // invalid
        	emailInput.setText("bob@bob.");
        	assertFalse(emailStyle.contains("valid")); // invalid
        	emailInput.setText("bob@bob.");
        	assertFalse(emailStyle.contains("valid")); // invalid
        	emailInput.setText("b;b@bob.com");
        	assertFalse(emailStyle.contains("valid")); // invalid
        	emailInput.setText("bobb@bo!b.com");
        	assertFalse(emailStyle.contains("valid")); // invalid
        	emailInput.setText("b&!#$%&'*+-/=?^_`{|}~b@bob.com");
        	assertTrue(emailStyle.contains("valid")); // valid (https://en.wikipedia.org/wiki/Email_address#Local-part)
        	emailInput.setText("bob@bob.com");
        	assertTrue(emailStyle.contains("valid"));
        	
        	var passwordStyle = lookup("#fieldPassword").queryAs(Region.class).getStyleClass();
        	var passwordInput = lookup("#fieldPasswordInput").queryTextInputControl();
        	
        	passwordInput.setText("bob");
        	assertFalse(passwordStyle.contains("valid")); // invalid
        	passwordInput.setText("bobddddddddDSSSDSDDDDDDSSSd");
        	assertFalse(passwordStyle.contains("valid")); // invalid
        	passwordInput.setText("");
        	assertFalse(passwordStyle.contains("valid")); // invalid
        	passwordInput.setText("bobddddddddDSSSDSDDDDDDSSSdàààQSDSQDQSà)====!:;==");
        	assertFalse(passwordStyle.contains("valid"));// invalid
        	passwordInput.setText("123456456546456456465645654654)====!:;==");
        	assertFalse(passwordStyle.contains("valid"));// invalid
        	passwordInput.setText("123456456546456456465645654654");
        	assertFalse(passwordStyle.contains("valid"));// invalid
        	passwordInput.setText("SDDSDssqdsqqsdsdq====!:;==ssqdsdqsq");
        	assertFalse(passwordStyle.contains("valid"));// invalid
        	passwordInput.setText("45665454s5445445");
        	assertTrue(passwordStyle.contains("valid"));
        	passwordInput.setText("45665454S5445445");
        	assertTrue(passwordStyle.contains("valid"));
        	passwordInput.setText("bobddddddddDSSSDSDDDDDDSSSd1");
        	assertTrue(passwordStyle.contains("valid"));
        	
        	var confirmPasswordStyle = lookup("#fieldConfirmPassword").queryAs(Region.class).getStyleClass();
        	var confirmPasswordInput = lookup("#fieldConfirmPasswordInput").queryTextInputControl();
        	assertFalse(confirmPasswordStyle.contains("valid")); // invalid
        	confirmPasswordInput.setText("");
        	assertFalse(confirmPasswordStyle.contains("valid")); // invalid
        	confirmPasswordInput.setText("bobddddddddDSSSDSDDDDDDSSSd1");
        	assertTrue(confirmPasswordStyle.contains("valid"));
    	});
    }

    
    @Test
    public void registration_form_submit_button_activation() {
    	try {
			RegisteredUserDAO.populateTable(); // Re-initialize data source with test data
		} catch (SQLException e) {
			fail("Database connection error.");
		}
    	Platform.runLater(()->{
        	control.showRegion(new UserAccountRegisterForm());
        	var usernameStyle = lookup("#fieldUsername").queryAs(Region.class).getStyleClass();
        	var usernameInput = lookup("#fieldUsernameInput").queryTextInputControl();
        	

        	usernameInput.setText("bobby");
        	assertTrue(usernameStyle.contains("valid"));
        	

        	var emailStyle = lookup("#fieldEmail").queryAs(Region.class).getStyleClass();
        	var emailInput = lookup("#fieldEmailInput").queryTextInputControl();
        	
        	emailInput.setText("bob@bob.com");
        	assertTrue(emailStyle.contains("valid"));
        	
        	var passwordStyle = lookup("#fieldPassword").queryAs(Region.class).getStyleClass();
        	var passwordInput = lookup("#fieldPasswordInput").queryTextInputControl();

        	passwordInput.setText("bobddddddddDSSSDSDDDDDDSSSd1");
        	assertTrue(passwordStyle.contains("valid"));
        	
        	var confirmPasswordStyle = lookup("#fieldConfirmPassword").queryAs(Region.class).getStyleClass();
        	var confirmPasswordInput = lookup("#fieldConfirmPasswordInput").queryTextInputControl();

        	confirmPasswordInput.setText("bobddddddddDSSSDSDDDDDDSSSd1");
        	assertTrue(confirmPasswordStyle.contains("valid"));
        	
        	var submitButton =lookup("#registerSubmitButton").queryButton();

        	assertFalse(submitButton.isDisable()); // Button enabled because all fields valid
        	
        	confirmPasswordInput.setText("bobddddddddDSSSDSDDDDDDSSSd1d"); // now confirm password doesn't match
        	assertFalse(confirmPasswordStyle.contains("valid"));
        	
        	assertTrue(submitButton.isDisable()); // Button disabled
        	
        	passwordInput.setText("bobddddddddDSSSDSDDDDDDSSSd1d"); // passwords match again
        	assertTrue(confirmPasswordStyle.contains("valid")); // Check confirm password validity was updated when password changed
        	
        	assertFalse(submitButton.isDisable()); // Button enabled because all fields valid
        	
        	emailInput.setText("bob@.com");
        	assertFalse(emailStyle.contains("valid"));
        	
        	assertTrue(submitButton.isDisable()); // Button disabled
        	
        	emailInput.setText("bob@d.com");
        	assertTrue(emailStyle.contains("valid"));
        	
        	assertFalse(submitButton.isDisable()); // Button enabled
        	
        	usernameInput.setText("judy");
        	assertFalse(usernameStyle.contains("valid"));
        	
        	assertTrue(submitButton.isDisable()); // Button disabled
        	
    	});
    }
    
    @Test
    public void information_form_validation() {
    	try {
			RegisteredUserDAO.populateTable(); // Re-initialize data source with test data
		} catch (SQLException e) {
			fail("Database connection error.");
		}
    	Platform.runLater(()->{
    		control.logInUser(RegisteredUserDAO.find("username", "bob"), "2bobb2ypassdddddddd");
        	var usernameStyle = lookup("#fieldModifyUsername").queryAs(Region.class).getStyleClass();
        	var usernameInput = lookup("#fieldModifyUsernameInput").queryTextInputControl();
        	
        	usernameInput.setText("bobby");
        	assertTrue(usernameStyle.contains("valid"));
        	usernameInput.setText(""); 
        	assertFalse(usernameStyle.contains("valid"));
        	usernameInput.setText("b@bby");
        	assertFalse(usernameStyle.contains("valid"));
        	usernameInput.setText("b;bby");
        	assertFalse(usernameStyle.contains("valid"));
        	usernameInput.setText("judy"); 
        	assertFalse(usernameStyle.contains("valid")); // taken
        	usernameInput.setText("bob"); 
        	assertTrue(usernameStyle.contains("valid")); // Same username as before
        	usernameInput.setText("bobster"); 
        	assertTrue(usernameStyle.contains("valid")); 

        	var emailStyle = lookup("#fieldModifyEmail").queryAs(Region.class).getStyleClass();
        	var emailInput = lookup("#fieldModifyEmailInput").queryTextInputControl();
        	
        	emailInput.setText("bob@.com");
        	assertFalse(emailStyle.contains("valid"));
        	emailInput.setText("");
        	assertFalse(emailStyle.contains("valid"));
        	emailInput.setText("dsq@d.dd");
        	assertFalse(emailStyle.contains("valid")); // taken
        	emailInput.setText("bobby@bob.bob");
        	assertTrue(emailStyle.contains("valid")); // Same email as before
        	emailInput.setText("bob@bob.com");
        	assertTrue(emailStyle.contains("valid"));
        	
        	var oldPasswordStyle = lookup("#fieldOldPassword").queryAs(Region.class).getStyleClass();
        	var oldPasswordInput = lookup("#fieldOldPasswordInput").queryTextInputControl();

        	oldPasswordInput.setText("!");
        	assertFalse(oldPasswordStyle.contains("valid"));
        	oldPasswordInput.setText("");
        	assertFalse(oldPasswordStyle.contains("valid"));
        	oldPasswordInput.setText("FAFAFAFEFDSQDFDFDFDFDFDFSDDDS");
        	assertFalse(oldPasswordStyle.contains("valid"));
        	oldPasswordInput.setText("2bobb2ypassdddddddd");
        	assertTrue(oldPasswordStyle.contains("valid"));
        	
        	var passwordStyle = lookup("#fieldModifyPassword").queryAs(Region.class).getStyleClass();
        	var passwordInput = lookup("#fieldModifyPasswordInput").queryTextInputControl();
        	var confirmPasswordStyle = lookup("#fieldConfirmModifyPassword").queryAs(Region.class).getStyleClass();
        	var confirmPasswordInput = lookup("#fieldConfirmModifyPasswordInput").queryTextInputControl();
        	var submitButton =lookup("#saveButton").queryButton();
        	
        	
        	passwordInput.setText("");
        	confirmPasswordInput.setText("");
        	assertTrue(passwordStyle.contains("valid"));
        	assertTrue(confirmPasswordStyle.contains("valid"));
        	
        	passwordInput.setText("12345645465546564!!!!!!");
        	assertFalse(passwordStyle.contains("valid"));
        	assertFalse(confirmPasswordStyle.contains("valid"));
        	
        	passwordInput.setText("12345645465546564!!!!!!abc");
        	assertTrue(passwordStyle.contains("valid"));
        	assertFalse(confirmPasswordStyle.contains("valid"));

        	assertTrue(submitButton.isDisable());
        	
        	confirmPasswordInput.setText("12345645465546564!!!!!!abc");
        	assertTrue(confirmPasswordStyle.contains("valid"));
        	
        	assertFalse(submitButton.isDisable());
    	});
    }
    
    @Test
    public void information_form_submit_button_activation() {
    	try {
			RegisteredUserDAO.populateTable(); // Re-initialize data source with test data
		} catch (SQLException e) {
			fail("Database connection error.");
		}
    	Platform.runLater(()->{
    		control.logInUser(RegisteredUserDAO.find("username", "bob"), "2bobb2ypassdddddddd");
        	var usernameStyle = lookup("#fieldModifyUsername").queryAs(Region.class).getStyleClass();
        	var usernameInput = lookup("#fieldModifyUsernameInput").queryTextInputControl();
        	var emailStyle = lookup("#fieldModifyEmail").queryAs(Region.class).getStyleClass();
        	var emailInput = lookup("#fieldModifyEmailInput").queryTextInputControl();
        	var oldPasswordStyle = lookup("#fieldOldPassword").queryAs(Region.class).getStyleClass();
        	var oldPasswordInput = lookup("#fieldOldPasswordInput").queryTextInputControl();
        	var passwordStyle = lookup("#fieldModifyPassword").queryAs(Region.class).getStyleClass();
        	var passwordInput = lookup("#fieldModifyPasswordInput").queryTextInputControl();
        	var confirmPasswordStyle = lookup("#fieldConfirmModifyPassword").queryAs(Region.class).getStyleClass();
        	var confirmPasswordInput = lookup("#fieldConfirmModifyPasswordInput").queryTextInputControl();
        	var submitButton =lookup("#saveButton").queryButton();
        	
        	
        	usernameInput.setText("bobster"); 
        	assertTrue(usernameStyle.contains("valid")); 

        	emailInput.setText("bob@bob.com");
        	assertTrue(emailStyle.contains("valid"));
        	
        	oldPasswordInput.setText("2bobb2ypassdddddddd");
        	assertTrue(oldPasswordStyle.contains("valid"));
        	
        	passwordInput.setText("");
        	assertTrue(passwordStyle.contains("valid"));
        	
        	confirmPasswordInput.setText("");
        	assertTrue(confirmPasswordStyle.contains("valid"));

        	assertFalse(submitButton.isDisable());
        	
        	confirmPasswordInput.setText("abc132SSSSSSSSSSSSSSSSS");
        	assertFalse(confirmPasswordStyle.contains("valid"));
        	
        	assertTrue(submitButton.isDisable());
        	
        	passwordInput.setText("abc132SSSSSSSSSSSSSSSSS");
        	assertTrue(passwordStyle.contains("valid"));
        	
        	assertFalse(submitButton.isDisable());
        	
        	oldPasswordInput.setText("aaaaaaaaa");
        	assertFalse(oldPasswordStyle.contains("valid"));
        	
        	assertTrue(submitButton.isDisable());
        	
        	oldPasswordInput.setText("2bobb2ypassdddddddd");
        	assertTrue(oldPasswordStyle.contains("valid"));
        	
        	assertFalse(submitButton.isDisable());
        	
        	emailInput.setText("bob@.com");
        	assertFalse(emailStyle.contains("valid"));
        	
        	assertTrue(submitButton.isDisable());
        	
        	emailInput.setText("bob@bobby.com");
        	assertTrue(emailStyle.contains("valid"));
        	
        	assertFalse(submitButton.isDisable());
        	
        	usernameInput.setText("@@@bobby@@@"); 
        	assertFalse(usernameStyle.contains("valid"));

        	assertTrue(submitButton.isDisable());
        	
        	usernameInput.setText("bobby"); 
        	assertTrue(usernameStyle.contains("valid")); 
        	
        	assertFalse(submitButton.isDisable());
    	});

    }
    @Test
    public void password_matching() {
    	try {
			RegisteredUserDAO.populateTable(); // Re-initialize data source with test data
		} catch (SQLException e) {
			fail("Database connection error.");
		}
    	Platform.runLater(()->{
    		var user = RegisteredUserDAO.find("username", "bob");

    		assertTrue(user.isPassword("2bobb2ypassdddddddd"));
    		assertFalse(user.isPassword(""));
    		assertFalse(user.isPassword("2bobb2ypassdddddddd "));
    		assertFalse(user.isPassword("2bobb2ypassddddddd"));
    	});

    }
    
}