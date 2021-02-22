package com.avereon.cartesia;

import com.avereon.cartesia.command.*;
import com.avereon.cartesia.snap.SnapCenter;
import com.avereon.cartesia.snap.SnapMidpoint;
import com.avereon.cartesia.snap.SnapNearest;
import com.avereon.util.Log;
import com.avereon.util.TextUtil;
import com.avereon.xenon.ActionLibrary;
import com.avereon.xenon.ActionProxy;
import com.avereon.xenon.ProgramProduct;
import javafx.scene.input.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandMap {

	private static final System.Logger log = Log.get();

	private static final Map<String, CommandMetadata> actionCommands = new ConcurrentHashMap<>();

	private static final Map<String, String> commandActions = new ConcurrentHashMap<>();

	private static final Map<CommandEventKey, String> eventActions = new ConcurrentHashMap<>();

	public static void load( ProgramProduct product ) {
		// High level letters
		// a - arc
		// c - circle
		// d - draw (size, paint, pattern, etc.)
		// e - ellipse
		// f - fill (paint, pattern, pattern offset, etc.)
		// g - grid
		// k - color/paint
		// l - line
		// p - point
		// s - snap
		// t - text
		// v - curve
		// w - path?
		// y - layer
		// z - zoom

		// Basic command
		add( product, "select", Select.class );

		// View commands
		add( product, "camera-move", CameraMove.class );
		//add( product, "camera-spin", CameraSpinCommand.class );
		add( product, "camera-view-point", CameraViewPoint.class );
		//add( product, "camera-walk", CameraWalkCommand.class );
		add( product, "camera-zoom", CameraZoom.class );
		//add( product, "camera-zoom-all", CameraZoomAllCommand.class );
		add( product, "camera-zoom-in", CameraZoomIn.class );
		add( product, "camera-zoom-out", CameraZoomOut.class );
		//add( product, "camera-zoom-window", ZoomWindowCommand.class );

		// Grid commands
		// gg - grid toggle
		add( product, "grid-toggle", GridToggle.class );

		// Measure commands
		//add( product, "measure-angle", MeasureAngleCommand.class );
		add( product, "measure-distance", MeasureDistance.class );

		// Draw setting commands
		// In GCAD these were initially managed with simple commands. Later on you could tell the
		// pattern changed to use grouped settings. There are multiple options here:
		// 1. Use simple commands for each setting. This could end up using a lot of commands
		//    unless three-letter commands (or two-letter with extra) are allowed.
		// 2. Use a single command to show the properties/settings for a context. Such as,
		//    DS for design draw/fill settings,
		//    YS for current layer draw/fill settings and
		//    LS or PS for new shape draw/fill settings.
		// 3. A combination of both ideas above where simple commands are defined for common actions.
		//
		// There are several settings each for draw and fill:
		//   Draw has size/width, paint/color, pattern, pattern offset, cap, join
		//   Fill has paint/color, pattern/image, pattern offset
		// design draw width
		// design draw paint
		// design fill paint
		// layer draw width
		// layer draw paint
		// layer fill paint
		// shape draw width
		// shape draw paint
		// shape fill paint

		// Shape commands
		add( product, "draw-arc-2", DrawArc2.class ); // center-endpoint-endpoint
		add( product, "draw-arc-3", DrawArc3.class ); // endpoint-midpoint-endpoint
		add( product, "draw-circle-2", DrawCircle2.class ); // center-radius
		//add( "draw-circle-3", Circle3Command.class ); // point-point-point
		add( product, "draw-curve-4", DrawCurve4.class ); // endpoint-midpoint-midpoint-endpoint
		//add( "draw-ellipse-3", EllipseCommand.class ); // center-radius/start-extent
		//add( "draw-ellipse-5", Ellipse5Command.class ); // point-point-point-point-point
		//add( "draw-ellipse-arc-5", EllipseArc5Command.class ); // center-radius-radius-start-extent
		add( product, "draw-line-2", DrawLine2.class ); // endpoint-endpoint
		add( product, "draw-line-perpendicular", DrawLinePerpendicular.class ); // shape-endpoint-endpoint
		add( product, "draw-marker", DrawMarker.class ); // point
		add( product, "draw-path", Path.class );

		// Layer commands
		add( product, "layer-create", LayerCreate.class );
		add( product, "layer-show", LayerShow.class );
		add( product, "layer-hide", LayerHide.class );
		add( product, "layer-sublayer", LayerSubLayer.class );
		add( product, "layer-delete", LayerDelete.class );
		add( product, "layer-toggle", LayerToggle.class );

		// Snap commands
		//add( product, "snap-grid", SnapSelectCommand.class, new SnapGrid() ); // No one really does this
		add( product, "snap-center", SnapSelect.class, new SnapCenter() );
		add( product, "snap-midpoint", SnapSelect.class, new SnapMidpoint() );
		add( product, "snap-nearest", SnapSelect.class, new SnapNearest() );

		// Snap auto commands
		//add( product, "snap-auto-grid", SnapAutoCommand.class, new SnapGrid() ); // No one really does this
		add( product, "snap-grid-toggle", SnapGridToggle.class );
		add( product, "snap-auto-nearest", SnapAuto.class, new SnapNearest() );

		add( product, "trim-single", TrimExtend.class );
		add( product, "trim-multi", TrimExtendMulti.class );

		// Event type actions
		add( new CommandEventKey( MouseEvent.MOUSE_PRESSED, MouseButton.PRIMARY ), "select" );
		add( new CommandEventKey( MouseEvent.MOUSE_PRESSED, MouseButton.PRIMARY, true, false, false, false ), "select" );
		add( new CommandEventKey( MouseEvent.MOUSE_PRESSED, MouseButton.SECONDARY ), "snap-auto-nearest" );

		// FIXME Conflicts with select
		//add( new CommandEventKey( MouseEvent.MOUSE_PRESSED, MouseButton.PRIMARY, true, false, false, false ), "camera-spin" );
		add( new CommandEventKey( MouseEvent.MOUSE_PRESSED, MouseButton.PRIMARY, false, true, false, false ), "camera-move" );
		add( new CommandEventKey( ScrollEvent.SCROLL ), "camera-zoom" );
		add( new CommandEventKey( ZoomEvent.ZOOM ), "camera-zoom" );
		add( new CommandEventKey( ScrollEvent.SCROLL, true, false, false, false ), "camera-walk" );
		add( new CommandEventKey( ZoomEvent.ZOOM, true, false, false, false ), "camera-walk" );

		printCommandMapByCommand();
		//printCommandMapByName();
	}

	private static void printCommandMapByCommand() {
		actionCommands.values().stream().sorted().forEach( k -> {
			StringBuilder builder = new StringBuilder();
			builder.append( k.getCommand() == null ? "  " : k.getCommand() ).append( " -> " ).append( k.getName() );
			if( k.getShortcut() != null ) builder.append( " <" ).append( k.getShortcut() ).append( ">" );
			builder.append( " [" ).append( k.getAction() ).append( "]" );
			System.out.println( builder );
		} );
	}

	public static boolean hasCommand( String shortcut ) {
		return commandActions.containsKey( shortcut );
	}

	public static CommandMetadata get( String shortcut ) {
		return getActionCommand( commandActions.getOrDefault( shortcut, TextUtil.EMPTY ) );
	}

	public static CommandMetadata get( InputEvent event ) {
		return getActionCommand( eventActions.getOrDefault( CommandEventKey.of( event ), TextUtil.EMPTY ) );
	}

	private static CommandMetadata getActionCommand( String action ) {
		if( TextUtil.isEmpty( action ) ) return null;
		CommandMetadata mapping = actionCommands.get( action );
		if( mapping == null ) log.log( Log.WARN, "No command for action: " + action );
		return mapping;
	}

	private static void add( CommandEventKey key, String action ) {
		eventActions.put( key, action );
	}

	private static void add( ProgramProduct product, String action, Class<? extends Command> type, Object... parameters ) {
		ActionLibrary library = product.getProgram().getActionLibrary();
		library.register( product.rb(), action );
		ActionProxy proxy = library.getAction( action );

		String name = proxy.getName();
		String command = null;
		if( proxy.getCommand() != null ) command = proxy.getCommand().toLowerCase();
		String shortcut = proxy.getShortcut();

		if( !actionCommands.containsKey( action ) ) {
			if( command != null ) commandActions.put( command, action );
			actionCommands.put( action, new CommandMetadata( action, name, command, shortcut, type, parameters ) );
		} else {
			CommandMetadata existing = actionCommands.get( action );
			log.log(
				Log.ERROR,
				"Shortcut already used: shortcut={0} action={1} conflict={2} existing={3}",
				command,
				action,
				type.getName(),
				existing.getType().getName()
			);
		}
	}

}
