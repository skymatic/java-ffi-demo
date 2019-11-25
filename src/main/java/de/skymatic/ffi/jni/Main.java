package de.skymatic.ffi.jni;

import de.skymatic.ffi.common.Dimensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(de.skymatic.ffi.panama.Main.class);

	public static void main(String[] args) {
		System.loadLibrary("PngWrapper"); // loads libPngWrapper.dylib
		PngWrapper lib = new PngWrapper();

		LOG.info("libpng version {}", lib.getVersion());
		Dimensions dimensions = lib.getDimensions("src/main/resources/bot.png");
		LOG.info("png dimensions are {}x{} px", dimensions.width, dimensions.height);
	}
}
