package form;

public class ValidatedUsernameField  extends ValidatedField{
    public ValidatedUsernameField() {
		super();
    	this.addValidator(
			(val) -> val.matches("^[\\w-]+$"),
			"Constitué de lettres non-accentuées, de chiffres, et des signes moins et sous-tiret."
		);
	}
}
