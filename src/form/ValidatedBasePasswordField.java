package form;

import javafx.scene.control.PasswordField;

public class ValidatedBasePasswordField  extends ValidatedField{
    public ValidatedBasePasswordField() {
		super();
		this.getChildren().remove(this.field);
		this.field = new PasswordField();
		this.getChildren().add(this.field);
	}
}
