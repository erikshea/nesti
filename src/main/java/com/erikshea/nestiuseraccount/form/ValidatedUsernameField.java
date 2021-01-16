package com.erikshea.nestiuseraccount.form;

/**
 *	Validated username field
 */
public class ValidatedUsernameField  extends ValidatedField{
	@Override
    public void initialize() {
		super.initialize();

    	this.addValidator(
			(val) -> val.matches("^[\\w-]+$"), // At least 1 character. Only a-z, A-Z, -, _
			"Constitué de lettres non-accentuées, de chiffres, et des signes moins et sous-tiret."
		);
	}
}
