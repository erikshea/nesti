package form;

/**
 *	Validated username field
 */
public class ValidatedUsernameField  extends ValidatedField{
    public ValidatedUsernameField() {
		super();
    	this.addValidator(
			(val) -> val.matches("^[\\w-]+$"), // At least 1 character. Only a-z, A-Z, -, _
			"Constitu� de lettres non-accentu�es, de chiffres, et des signes moins et sous-tiret."
		);
	}
}
