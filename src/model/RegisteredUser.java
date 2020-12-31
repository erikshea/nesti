package model;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Version;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;

public class RegisteredUser implements Cloneable{
	private IntegerProperty userId;
    private StringProperty username;
    private StringProperty email;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty city;
    private StringProperty passwordHash;
    private StringProperty registrationDate;
    
    //Constructor
    public RegisteredUser() {
        this.userId = new SimpleIntegerProperty();
        this.username = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.passwordHash = new SimpleStringProperty();
        
        var formatter = new SimpleDateFormat("dd/MM/yyyy");  
        this.registrationDate = new SimpleStringProperty(formatter.format(new Date()));
    }

    public RegisteredUser(String username, String email, String firstName, String lastName, String city, String plaintextPassword) {
    	this();
    	this.setUsername(username);
    	this.setEmail(email);
    	this.setFirstName(firstName);
    	this.setLastName(lastName);
    	this.setCity(city);
    	this.setPasswordHashFromPlainText(plaintextPassword);
    }
    
    @Override
    public RegisteredUser clone() {
		RegisteredUser user = new RegisteredUser(
			this.getUsername(),
			this.getEmail(),
			this.getFirstName(),
			this.getLastName(),
			this.getCity(),
			this.getPasswordHash()
    	);
    	user.setUserId(this.getUserId());
    	user.setRegistrationDate(this.getRegistrationDate());
        return user;
    }
    
	public Integer getUserId() {
		return userId.get();
	}

	public void setUserId(Integer user_id) {
		this.userId.set(user_id);
	}

	public String getUsername() {
		return username.get();
	}

	public void setUsername(String user_name) {
		this.username.set(user_name);
	}

	public String getEmail() {
		return email.get();
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String first_name) {
		this.firstName.set(first_name);
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String last_name) {
		this.lastName.set(last_name);
	}

	public String getCity() {
		return city.get();
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public String getPasswordHash() {
		return passwordHash.get();
	}

	public void setPasswordHash(String password_hash) {
		this.passwordHash.set(password_hash);
	}

	public String getRegistrationDate() {
		return registrationDate.get();
	}

	public void setRegistrationDate(String register_date) {
		this.registrationDate.set(register_date);
	}
	
	public void setPasswordHashFromPlainText(String plaintextPassword) {
		var hash = BCrypt.with(LongPasswordStrategies.truncate(Version.VERSION_2A))
			.hashToString(6, plaintextPassword.toCharArray());
		this.setPasswordHash(hash);
	}

	public boolean isPassword(String plaintextPassword) {
		var verifyer = BCrypt.verifyer(Version.VERSION_2A, LongPasswordStrategies.truncate(Version.VERSION_2A));
		return verifyer.verify(plaintextPassword.toCharArray(), this.getPasswordHash()).verified;
	}

	public final StringProperty getUsernameProperty() {
		return this.username;
	}
	
	public final StringProperty getEmailProperty() {
		return this.email;
	}
	
	public final StringProperty getFirstNameProperty() {
		return this.firstName;
	}
	
	public final StringProperty getLastNameProperty() {
		return this.lastName;
	}
	
	public final StringProperty getCityProperty() {
		return this.city;
	}
	
	public final StringProperty getPasswordHashProperty() {
		return this.passwordHash;
	}
	
	public final StringProperty getRegistrationDateProperty() {
		return this.registrationDate;
	}
	
	
}
