package com.avereon.cartesia.command;

import com.avereon.cartesia.Command;
import com.avereon.cartesia.CommandEventKey;
import com.avereon.cartesia.tool.CommandContext;
import com.avereon.cartesia.tool.DesignTool;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;

public class SelectCommand extends Command {

	private CommandEventKey eventKey;

	private Point3D dragAnchor;

	@Override
	public Object execute( CommandContext context, DesignTool tool, Object... parameters ) {
		if( parameters.length < 1 ) return complete();

		if( parameters[ 0 ] instanceof MouseEvent ) {
			MouseEvent event = (MouseEvent)parameters[ 0 ];
			if( event.getEventType() == MouseEvent.MOUSE_PRESSED ) {
				return mousePressed( context, tool, event );
			} else if( event.getEventType() == MouseEvent.MOUSE_RELEASED ) {
				return mouseReleased( context, tool, event );
			}
		}

		// For this command the incoming parameter is the mouse event that triggered it
		return complete();
	}

	private Object mousePressed( CommandContext context, DesignTool tool, MouseEvent event ) {
		context.setAnchor( tool.mouseToWorkplane( event.getX(), event.getY(), event.getZ() ) );
		eventKey = CommandEventKey.of( event );
		dragAnchor = new Point3D( event.getX(), event.getY(), 0 );
		return incomplete();
	}

	private Object mouseReleased( CommandContext context, DesignTool tool, MouseEvent event ) {
		if( context.isPenMode() ) {
			return tool.mouseToWorkplane( event.getX(), event.getY(), event.getZ() );
		} else if( context.isSingleSelectMode( event ) ) {
			tool.mouseSelect( event.getX(), event.getY(), event.getZ(), isSelectToggleEvent( event ) );
			return complete();
		} else if( context.isWindowSelectMode( event ) ) {
			tool.windowSelect( dragAnchor, new Point3D( event.getX(), event.getY(), event.getZ() ), isSelectByContains( event ) );
			return complete();
		}
		return complete();
	}

	@Override
	public void handle( MouseEvent event ) {
		DesignTool tool = (DesignTool)event.getSource();
		if( eventKey != null ) {
			if( event.getEventType() == MouseEvent.MOUSE_DRAGGED && event.getButton() == eventKey.getButton() ) {
				tool.updateSelectWindow( dragAnchor, new Point3D( event.getX(), event.getY(), event.getZ() ) );
			} else if( event.getEventType() == MouseEvent.MOUSE_RELEASED && event.getButton() == eventKey.getButton() ) {
				tool.updateSelectWindow( dragAnchor, dragAnchor );
				tool.getCommandContext().resubmit( tool, this, event );
			}
		}
	}

	private boolean isSelectToggleEvent( MouseEvent event ) {
		return event.isControlDown();
	}

	private boolean isSelectByContains( MouseEvent event ) {
		return !event.isControlDown();
	}

}
