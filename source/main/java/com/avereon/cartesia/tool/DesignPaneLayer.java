package com.avereon.cartesia.tool;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;

/**
 * This is the internal layer that represents the design layer.
 */
public class DesignPaneLayer extends Pane {

	private final BooleanProperty showing;

	public DesignPaneLayer() {
		showing = new SimpleBooleanProperty( isVisible() );
	}

	public boolean isShowing() {
		return showing != null && showing.get();
	}

	public BooleanProperty showingProperty() {
		return showing;
	}

}