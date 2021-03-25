package com.avereon.cartesia.command;

import com.avereon.cartesia.data.DesignLine;
import com.avereon.cartesia.tool.CommandContext;
import com.avereon.cartesia.tool.DesignTool;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;

public class Copy extends EditCommand {

	private DesignLine referenceLine;

	private Point3D anchor;

	@Override
	public Object execute( CommandContext context, DesignTool tool, Object... parameters ) throws Exception {
		if( tool.selectedShapes().isEmpty() ) return COMPLETE;

		setCaptureUndoChanges( tool, false );

		// Ask for an anchor point
		if( parameters.length < 1 ) {
			addReference( tool, referenceLine = new DesignLine( context.getWorldMouse(), context.getWorldMouse() ) );
			promptForPoint( context, tool, "anchor" );
			return INCOMPLETE;
		}

		// TODO Make a shadow copy of the selected components to show as a preview

		// Ask for a target point
		if( parameters.length < 2 ) {
			addPreview( tool, cloneShapes( tool.getSelectedShapes() ) );
			tool.clearSelected();
			anchor = asPoint( context, parameters[ 0 ] );
			referenceLine.setPoint( anchor ).setOrigin( anchor );
			promptForPoint( context, tool, "target" );
			return INCOMPLETE;
		}

		clearReferenceAndPreview( tool );

		// Copy the selected shapes
		setCaptureUndoChanges( tool, true );
		// Start an undo multi-change
		copyShapes( tool.getSelectedShapes(), asPoint( context, parameters[ 0 ] ), asPoint( context, parameters[ 1 ] ) );
		// Done with undo multi-change

		return COMPLETE;
	}

	@Override
	public void handle( MouseEvent event ) {
		if( event.getEventType() == MouseEvent.MOUSE_MOVED ) {
			DesignTool tool = (DesignTool)event.getSource();
			Point3D point = tool.mouseToWorkplane( event.getX(), event.getY(), event.getZ() );
			switch( getStep() ) {
				case 1 -> referenceLine.setPoint( point ).setOrigin( point );
				case 2 -> {
					//moveShapes( getPreview(), point, anchor );
					referenceLine.setPoint( point );
					//moveShapes( getPreview(), anchor, point );
				}
			}
		}
	}

}
