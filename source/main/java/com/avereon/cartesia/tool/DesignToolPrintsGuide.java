package com.avereon.cartesia.tool;

import com.avereon.cartesia.BundleKey;
import com.avereon.cartesia.data.DesignNode;
import com.avereon.util.Log;
import com.avereon.xenon.Program;
import com.avereon.xenon.ProgramProduct;
import com.avereon.xenon.tool.guide.Guide;
import com.avereon.xenon.tool.guide.GuideNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DesignToolPrintsGuide extends Guide {

	private static final System.Logger log = Log.get();

	private final ProgramProduct product;

	private final DesignTool tool;

	private final Map<DesignNode, GuideNode> nodes;

	public DesignToolPrintsGuide( ProgramProduct product, DesignTool tool ) {
		this.product = product;
		this.tool = tool;
		this.nodes = new ConcurrentHashMap<>();
		setName( product.rb().textOr( BundleKey.LABEL, "prints", "Prints" ) );
	}

	ProgramProduct getProduct() {
		return product;
	}

	Program getProgram() {
		return product.getProgram();
	}

}