package de.skymatic.ffi.panama;

import java.foreign.NativeTypes;
import java.foreign.Scope;
import java.foreign.memory.Callback;
import java.foreign.memory.Pointer;

import de.skymatic.ffi.common.Dimensions;
import de.skymatic.libpng.png_lib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usr.include.stdio_lib;

/**
 * Tries to load libpng when accessing class. Run with <code>-Djava.library.path=/usr/local/lib</code>
 */
public class Main {
	
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		LOG.info("libpng version {}", Pointer.toString(png_lib.PNG_LIBPNG_VER_STRING));
		Dimensions dimensions = getDimensions("src/main/resources/bot.png");
		LOG.info("png dimensions are {}x{} px", dimensions.width, dimensions.height);
	}
	
	private static Dimensions getDimensions(String path) {
		try (Scope scope = Scope.globalScope().fork()) {
			// fopen
			var filename = scope.allocateCString(path);
			var mode = scope.allocateCString("r");
			var file = stdio_lib.fopen(filename, mode);
			assert !file.isNull() : "failed to fopen file";

			// png_read_info
			var png_ptr = png_lib.png_create_read_struct(png_lib.PNG_LIBPNG_VER_STRING, Pointer.ofNull(), Callback.ofNull(), Callback.ofNull());
			var info_ptr = png_lib.png_create_info_struct(png_ptr);
			png_lib.png_init_io(png_ptr, file);
			png_lib.png_read_info(png_ptr, info_ptr);

			// get dimensions
			var width = scope.allocate(NativeTypes.INT);
			var height = scope.allocate(NativeTypes.INT);
			png_lib.png_get_IHDR(png_ptr, info_ptr, width, height, Pointer.ofNull(), Pointer.ofNull(), Pointer.ofNull(), Pointer.ofNull(), Pointer.ofNull());
			
			// cleanup
			var info_ptr_ptr = scope.allocate(info_ptr.type().pointer());
			info_ptr_ptr.set(info_ptr);
			var png_ptr_ptr = scope.allocate(png_ptr.type().pointer());
			png_ptr_ptr.set(png_ptr);
			png_lib.png_destroy_read_struct(png_ptr_ptr, info_ptr_ptr, Pointer.ofNull());

			// fclose
			stdio_lib.fclose(file);
			
			
			return new Dimensions(width.get(), height.get());
		}
	}
	
}
