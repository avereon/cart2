package com.avereon.cartesia.command;

import com.avereon.cartesia.data.DesignLayer;
import com.avereon.cartesia.tool.CommandContext;
import com.avereon.cartesia.tool.DesignTool;

public class LayerToggle extends LayerCommand {

	@Override
	public Object execute( CommandContext context, Object... parameters ) throws Exception {
		DesignTool tool = context.getTool();
		DesignLayer layer = tool.getCurrentLayer();
		tool.setLayerVisible( layer, !tool.isLayerVisible( layer ) );
		return COMPLETE;
	}

}
