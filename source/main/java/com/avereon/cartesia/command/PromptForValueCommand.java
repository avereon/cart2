package com.avereon.cartesia.command;

import com.avereon.cartesia.tool.CommandContext;
import com.avereon.cartesia.tool.DesignTool;

public class PromptForValueCommand extends PromptCommand {

	public PromptForValueCommand( String prompt ) {
		super( prompt );
	}

	@Override
	public Object execute( CommandContext context, DesignTool tool, Object... parameters ) {
		return super.execute( context, tool, parameters );
	}

}
