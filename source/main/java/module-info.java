import com.avereon.cartesia.CartesiaMod;

module com.avereon.cartesia {
	requires com.avereon.xenon;
	requires jep;

	opens com.avereon.cartesia.bundles;
	opens com.avereon.cartesia.settings;

	exports com.avereon.cartesia to com.avereon.xenon, com.avereon.venza;
	exports com.avereon.cartesia.cursor to com.avereon.venza;

	provides com.avereon.xenon.Mod with CartesiaMod;
}