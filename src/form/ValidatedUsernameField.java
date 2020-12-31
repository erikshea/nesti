package form;

public class ValidatedUsernameField  extends ValidatedField{
    public ValidatedUsernameField() {
		super();
    	this.addValidator(
			(val) -> val.matches("^[\\w-]+$"),
			"Constitu� de lettres non-accentu�es, de chiffres, et des signes moins et sous-tiret."
		);
	}
}
