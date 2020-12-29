package model;

import javafx.beans.property.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Version;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;

public class RegisteredUser {
	private IntegerProperty user_id;
    private StringProperty username;
    private StringProperty email;
    private StringProperty first_name;
    private StringProperty last_name;
    private StringProperty city;
    private StringProperty password_hash;
    private StringProperty registration_date;
    
    //Constructor
    public RegisteredUser() {
        this.user_id = new SimpleIntegerProperty();
        this.username = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.first_name = new SimpleStringProperty();
        this.last_name = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.password_hash = new SimpleStringProperty();
        
        var formatter = new SimpleDateFormat("dd/MM/yyyy");  
        this.registration_date = new SimpleStringProperty(formatter.format(new Date()));
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
    
	public Integer getUserId() {
		return user_id.get();
	}

	public void setUserId(Integer user_id) {
		this.user_id.set(user_id);
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
		return first_name.get();
	}

	public void setFirstName(String first_name) {
		this.first_name.set(first_name);
	}

	public String getLastName() {
		return last_name.get();
	}

	public void setLastName(String last_name) {
		this.last_name.set(last_name);
	}

	public String getCity() {
		return city.get();
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public String getPasswordHash() {
		return password_hash.get();
	}

	public void setPasswordHash(String password_hash) {
		this.password_hash.set(password_hash);
	}

	public String getRegistrationDate() {
		return registration_date.get();
	}

	public void setRegistrationDate(String register_date) {
		this.registration_date.set(register_date);
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
}
