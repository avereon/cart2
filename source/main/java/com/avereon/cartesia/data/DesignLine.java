package com.avereon.cartesia.data;

import com.avereon.cartesia.ParseUtil;
import javafx.geometry.Point3D;

import java.util.Map;

public class DesignLine extends DesignShape {

	public static final String POINT = "point";

	public DesignLine() {
		addModifyingKeys( ORIGIN, POINT );
	}

	public DesignLine( Point3D origin, Point3D point ) {
		this();
		setOrigin( origin );
		setPoint( point );
	}

	public Point3D getPoint() {
		return getValue( POINT );
	}

	public DesignShape setPoint( Point3D point ) {
		setValue( POINT, point );
		return this;
	}

	protected Map<String, Object> asMap() {
		Map<String, Object> map = super.asMap();
		map.put( SHAPE, "line" );
		map.putAll( asMap( POINT ) );
		return map;
	}

	public DesignLine updateFrom( Map<String, Object> map ) {
		super.updateFrom( map );
		setPoint( ParseUtil.parsePoint3D( (String)map.get( POINT ) ) );
		return this;
	}

	@Override
	public String toString() {
		return super.toString( ORIGIN, POINT );
	}

	@Override
	public double distanceTo( Point3D point ) {
		// NEXT Compute distance to line
		//return Geometry.getPointLineDistance( a,b,p );

		return super.distanceTo( point );
	}

}
