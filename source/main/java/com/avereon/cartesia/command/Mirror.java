package com.avereon.cartesia.command;

import com.avereon.cartesia.data.DesignLine;
import com.avereon.cartesia.tool.CommandContext;
import com.avereon.cartesia.tool.DesignTool;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;

public class Mirror extends EditCommand {

	private DesignLine referenceLine;

	private Point3D anchor;

	private Point3D lastPoint;

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

		// Ask for a target point
		if( parameters.length < 2 ) {
			anchor = asPoint( context, parameters[ 0 ] );
			referenceLine.setPoint( anchor ).setOrigin( anchor );
			addPreview( tool, cloneShapes( tool.getSelectedShapes() ) );
			promptForPoint( context, tool, "target" );
			return INCOMPLETE;
		}

		clearReferenceAndPreview( tool );

		// Move the selected shapes
		setCaptureUndoChanges( tool, true );
		// Start an undo multi-change
		mirrorShapes( tool.getSelectedShapes(), asPoint( context, parameters[ 0 ] ), asPoint( context, parameters[ 1 ] ) );
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
					if( lastPoint == null ) lastPoint = anchor;
					referenceLine.setPoint( point );
					// FIXME How to mirror relative to the prior mirror is a problem
					mirrorShapes( getPreview(), lastPoint, point );
					lastPoint = point;
				}
			}
		}
	}


}
