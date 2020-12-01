package com.avereon.cartesia.data;

import com.avereon.cartesia.BundleKey;
import com.avereon.cartesia.DesignUnit;
import com.avereon.data.IdNode;
import com.avereon.data.Node;
import com.avereon.data.NodeComparator;
import com.avereon.data.NodeSettingsWrapper;
import com.avereon.util.Log;
import com.avereon.xenon.ProgramProduct;
import com.avereon.xenon.tool.settings.SettingsPage;
import com.avereon.xenon.tool.settings.SettingsPageParser;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DesignLayer extends DesignDrawable {

	private static final System.Logger log = Log.get();

	public static final String NAME = "name";

	public static final String UNIT = "unit";

	// NOTE Visibility is a setting in the design tool, not here

	public static final String LAYERS = "layers";

	public static final String SHAPES = "shapes";

	//private static final NodeComparator<DesignLayer> comparator;

//	static {
//		comparator = new NodeComparator<>( ORDER, NAME );
//	}

	public DesignLayer() {
		defineNaturalKey( NAME );
		addModifyingKeys( NAME, UNIT, LAYERS, SHAPES );
	}

	/**
	 * Overridden to return the specific type of this class.
	 *
	 * @param id The node id
	 * @return This instance
	 */
	public DesignLayer setId( String id ) {
		super.setValue( ID, id );
		return this;
	}

	public String getName() {
		return getValue( NAME );
	}

	public DesignLayer setName( String name ) {
		setValue( NAME, name );
		return this;
	}

	public DesignUnit getDesignUnit() {
		return getValue( UNIT );
	}

	public DesignLayer setDesignUnit( DesignUnit unit ) {
		setValue( UNIT, unit );
		return this;
	}

	public List<DesignLayer> getAllLayers() {
		List<DesignLayer> layers = new ArrayList<>();
		getLayers().forEach( layer ->  {
			layers.add( layer );
			layers.addAll( layer.getAllLayers() );
		} );
		return layers;
	}

	public Set<DesignLayer> findLayers( String key, Object value ) {
		return getAllLayers().stream().filter( l -> Objects.equals( l.getValue( key ), value ) ).collect( Collectors.toSet() );
	}

	public List<DesignLayer> getLayers() {
		return getValueList( LAYERS, new NodeComparator<>( DesignLayer.ORDER, DesignLayer.NAME ) );
	}

	public DesignLayer addLayer( DesignLayer layer ) {
		addToSet( LAYERS, layer );
		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public DesignLayer addLayerBeforeOrAfter( DesignLayer layer, DesignLayer anchor, boolean after ) {
		List<DesignLayer> layers = getLayers();

		int size = layers.size();
		int insert = anchor == null ? -1 : layers.indexOf( anchor );
		if( after ) insert++;
		if( insert < 0 || insert > size ) insert = size;
		layers.add( insert, layer );
		updateOrder( layers );
		addLayer( layer );

		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	private <T extends DesignDrawable> List<T> updateOrder( List<T> list ) {
		AtomicInteger counter = new AtomicInteger(0);
		list.forEach( i -> i.setOrder( counter.getAndIncrement() ) );
		return list;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public DesignLayer removeLayer( DesignLayer layer ) {
		removeFromSet( LAYERS, layer );
		return this;
	}

	public Set<DesignShape> getShapes() {
		return getValues( SHAPES );
	}

	public DesignLayer addShape( DesignShape shape ) {
		addToSet( SHAPES, shape );
		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public DesignLayer removeShape( DesignShape shape ) {
		removeFromSet( SHAPES, shape );
		return this;
	}

	@SuppressWarnings( "UnusedReturnValue" )
	public <T extends DesignDrawable> T addDrawable( T thingy ) {
		if( thingy instanceof DesignShape ) {
			addShape( (DesignShape)thingy );
		} else if( thingy instanceof DesignLayer ) {
			addLayer( (DesignLayer)thingy );
		}
		return thingy;
	}

	public <T extends DesignDrawable> T removeDrawable( T thingy ) {
		if( thingy instanceof DesignShape ) {
			removeShape( (DesignShape)thingy );
		} else if( thingy instanceof DesignLayer ) {
			removeLayer( (DesignLayer)thingy );
		}
		return thingy;
	}

	@Override
	public SettingsPage getPropertiesPage( ProgramProduct product ) throws IOException {
		String pagePath = "/com/avereon/cartesia/design/props/layer.xml";
		if( page == null ) page = new SettingsPageParser( product, new NodeSettingsWrapper( this ) ).parse( pagePath, BundleKey.PROPS ).get( "layer" );
		return page;
	}

	public Map<String, Object> asMap() {
		Map<String, Object> map = super.asMap();
		map.putAll( asMap( NAME ) );
		return map;
	}

	public Map<String, Object> asDeepMap() {
		Map<String, Object> map = new HashMap<>( asMap() );
		map.put( LAYERS, getLayers().stream().collect( Collectors.toMap( IdNode::getId, DesignLayer::asMap ) ) );
		map.put( SHAPES, getShapes().stream().collect( Collectors.toMap( IdNode::getId, DesignShape::asMap ) ) );
		return map;
	}

	public DesignLayer updateFrom( Map<String, Object> map ) {
		super.updateFrom( map );
		if( map.containsKey( NAME ) ) setName( (String)map.get( NAME ) );
		return this;
	}

	@Override
	public <T extends Node> Comparator<T> getComparator() {
		return new NodeComparator<>( ORDER, NAME );
	}

	@Override
	public String toString() {
		return super.toString( NAME );
	}

}
