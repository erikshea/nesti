package form;

import javafx.scene.control.PasswordField;

/**
 * Base field inherited by other password fields. Contains a PasswordField instead of a TextField
 */
public class ValidatedBasePasswordField  extends ValidatedField{
    public ValidatedBasePasswordField() {
		super();
		this.setField(new PasswordField());
	}
}
