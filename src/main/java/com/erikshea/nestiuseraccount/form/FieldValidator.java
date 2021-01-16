package com.erikshea.nestiuseraccount.form;

import java.util.function.Predicate;

/**
 * Object holds a message, and a validator function
 */
public class FieldValidator{
	private String message;
	private Predicate<String> validatorFunction;
	
	FieldValidator(String message, Predicate<String> validatorFunction){
		this.message = message; // Message to display under field
		this.validatorFunction = validatorFunction; // Function that must return true for field to validate
	}
	
	/**
	 * Check if value validates
	 * @param value value to check
	 * @return true if value validates
	 */
	public boolean validate(String value) { 
		return this.validatorFunction.test(value);
	}
	
	public String getMessage() {
		return this.message; 
	}
}
