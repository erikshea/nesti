package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.GridPane;

/**
 *	Base class for all panes that are to be shown inside main window
 */
public class ControlledGridPane extends GridPane{
	// Observable main controller object
	protected ObjectProperty<UserAccountControl> mainController = new SimpleObjectProperty<>();
	
    public void setMainController(UserAccountControl c) {
    	this.mainController.set(c);
    }
    
    public UserAccountControl getMainController() {
    	return this.mainController.get();
    }
    
    public final ObjectProperty<UserAccountControl> getMainControllerPropery() {
    	return this.mainController;
    }

	public ControlledGridPane() {
		this.needsLayoutProperty().addListener((e)->this.requestFocus());
	}
}

