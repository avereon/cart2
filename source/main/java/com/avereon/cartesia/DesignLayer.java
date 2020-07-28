package com.avereon.cartesia;

import com.avereon.data.Node;
import com.avereon.data.NodeComparator;
import javafx.scene.paint.Color;

import java.util.UUID;

public class DesignLayer extends Node implements Comparable<DesignLayer> {

	public static final String ID = "id";

	public static final String NAME = "name";

	public static final String UNIT = "unit";

	public static final String DRAW_WIDTH = "draw-width";

	public static final String DRAW_COLOR = "draw-color";

	public static final String FILL_COLOR = "fill-color";

	public static final String ORDER = "order";

	private static final NodeComparator<DesignLayer> comparator;

	static {
		comparator = new NodeComparator<>( ORDER, NAME );
	}

	public DesignLayer() {
		definePrimaryKey( ID );
		defineNaturalKey( NAME );
		addModifyingKeys( NAME, DRAW_WIDTH, DRAW_COLOR, FILL_COLOR );

		setId( UUID.randomUUID().toString() );
	}

	public String getId() {
		return getValue( ID );
	}

	public DesignLayer setId( String id ) {
		setValue( ID, id );
		return this;
	}

	public String getName() {
		return getValue( NAME );
	}

	public DesignLayer setName( String name ) {
		setValue( NAME, name );
		return this;
	}

	public double getOrder() {
		return getValue( ORDER, 0 );
	}

	public DesignLayer setOrder( double order ) {
		setValue( ORDER, order );
		return this;
	}

	public DesignUnit getDesignUnit() {
		return getValue( UNIT );
	}

	public DesignLayer setDesignUnit( DesignUnit unit ) {
		setValue( UNIT, unit );
		return this;
	}

	public double getDrawWidth() {
		return getValue( DRAW_WIDTH );
	}

	public DesignLayer setDrawWidth( double width ) {
		setValue( DRAW_WIDTH, width );
		return this;
	}

	public Color getDrawColor() {
		return getValue( DRAW_COLOR );
	}

	public DesignLayer setDrawColor( Color color ) {
		setValue( DRAW_COLOR, color );
		return this;
	}

	public Color getFillColor() {
		return getValue( FILL_COLOR );
	}

	public DesignLayer setFillColor( Color color ) {
		setValue( FILL_COLOR, color );
		return this;
	}

	@Override
	public int compareTo( DesignLayer that ) {
		return comparator.compare( this, that );
	}

}