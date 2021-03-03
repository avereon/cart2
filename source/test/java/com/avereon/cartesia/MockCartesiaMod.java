package com.avereon.cartesia;

import com.avereon.product.ProductBundle;
import com.avereon.product.ProductCard;
import com.avereon.settings.MapSettings;
import com.avereon.settings.Settings;
import com.avereon.xenon.Program;
import com.avereon.xenon.ProgramProduct;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class MockCartesiaMod implements ProgramProduct {

	@Override
	public ProductCard getCard() {
		return new ProductCard();
	}

	@Override
	public ClassLoader getClassLoader() {
		return getClass().getClassLoader();
	}

	@Override
	public ProductBundle rb() {
		return new ProductBundle( getProgram(), this );
	}

	@Override
	public Settings getSettings() {
		return new MapSettings();
	}

	@Override
	public Path getDataFolder() {
		return Paths.get( "." );
	}

	@Override
	public Program getProgram() {
		return null;
	}

	@Override
	public <T> void task( String name, Callable<T> callable ) {
		// This implementation is not suitable for production
		// but is very useful for unit testing
		try {
			callable.call();
		} catch( Exception exception ) {
			throw new RuntimeException( exception );
		}
	}

}
