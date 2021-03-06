package com.avereon.cartesia.math.el;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import java.util.Stack;

public class Deg extends PostfixMathCommand {

	public Deg() {
		super.numberOfParameters = 1;
	}

	@SuppressWarnings( "unchecked" )
	public void run( Stack stack ) throws ParseException {
		this.checkStack( stack );
		Object value = stack.pop();
		stack.push( this.toDegrees( value ) );
	}

	public Object toDegrees( Object value ) throws ParseException {
		if( value instanceof Number ) {
			return Math.toDegrees( ((Number)value).doubleValue() );
		} else {
			throw new ParseException( "Invalid parameter type" );
		}
	}


}
