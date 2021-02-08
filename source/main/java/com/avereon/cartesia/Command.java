package com.avereon.cartesia;

import com.avereon.cartesia.command.PromptCommand;
import com.avereon.cartesia.data.DesignShape;
import com.avereon.cartesia.math.Shapes;
import com.avereon.cartesia.math.Maths;
import com.avereon.cartesia.tool.CommandContext;
import com.avereon.cartesia.tool.DesignTool;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Command {

	public static final Object INCOMPLETE = new Object();

	public static final Object COMPLETE = new Object();

	public static final Object INVALID = new Object();

	private DesignShape preview;

	public Object execute( CommandContext context, DesignTool tool, Object... parameters ) throws Exception {
		return null;
	}

	public void cancel( DesignTool tool ) throws Exception {
		if( tool != null ) {
			tool.getCurrentLayer().removeShape( preview );
			tool.setCursor( Cursor.DEFAULT );
			tool.getDesign().clearSelected();
		}
	}

	public boolean isInputCommand() {
		return false;
	}

	public void handle( KeyEvent event ) {}

	public void handle( MouseEvent event ) {}

	protected Object invalid() {
		return INVALID;
	}

	protected Object incomplete() {
		return INCOMPLETE;
	}

	protected Object complete() {
		return COMPLETE;
	}

	protected double asDouble( Object value ) throws Exception {
		if( value instanceof Double ) return (Double)value;
		if( value instanceof Point3D ) return ((Point3D)value).distance( Point3D.ZERO );
		return Maths.eval( String.valueOf( value ) );
	}

	protected double asDouble( Point3D anchor, Object value ) throws Exception {
		if( value instanceof Double ) return (Double)value;
		if( value instanceof Point3D ) return ((Point3D)value).distance( anchor );
		return Maths.eval( String.valueOf( value ) );
	}

	protected Point3D asPoint( DesignTool tool, Object value, Point3D anchor ) throws Exception {
		if( value instanceof Point3D ) return (Point3D)value;
		return Shapes.parsePoint( String.valueOf( value ), anchor );
	}

	protected void promptForNumber( CommandContext context, DesignTool tool, String bundleKey, String key ) {
		tool.setCursor( tool.getReticle() );
		promptForValue( context, tool, bundleKey, key );
	}

	protected void promptForPoint( CommandContext context, DesignTool tool, String bundleKey, String key ) {
		tool.setCursor( tool.getReticle() );
		promptForValue( context, tool, bundleKey, key );
	}

	protected void promptForText( CommandContext context, DesignTool tool, String bundleKey, String key ) {
		tool.setCursor( Cursor.TEXT );
		promptForValue( context, tool, bundleKey, key );
	}

	protected DesignShape selectNearestShapeAtMouse( CommandContext context, DesignTool tool ) {
		return selectNearestShapeAtPoint( tool, context.getMouse() );
	}

	protected DesignShape selectNearestShapeAtPoint( DesignTool tool, Point3D point ) {
		return Shapes.findNearestShapeToPoint( tool.selectShapes( point ), point );
	}

	protected void setPreview( DesignTool tool, DesignShape preview ) {
		this.preview = preview;
		tool.getAsset().setCaptureUndoChanges( false );
		tool.getCurrentLayer().addShape( preview );
		preview.setPreview( true );
	}

	@SuppressWarnings( "unchecked" )
	protected <T extends DesignShape> T getPreview() {
		return (T)preview;
	}

	protected Object commitPreview( DesignTool tool ) {
		tool.getCurrentLayer().removeShape( preview );
		preview.setPreview( false );
		tool.getAsset().setCaptureUndoChanges( true );
		tool.getCurrentLayer().addShape( preview );
		return complete();
	}

	protected Object removePreview( DesignTool tool ) {
		tool.getCurrentLayer().removeShape( preview );
		preview.setPreview( false );
		return complete();
	}

	private void promptForValue( CommandContext context, DesignTool tool, String bundleKey, String key ) {
		String prompt = context.getProduct().rb().text( bundleKey, key );
		context.submit( tool, new PromptCommand( prompt ) );
	}

}
