package de.skymatic.ffi.jni;

import de.skymatic.ffi.common.Dimensions;

/**
 * Used to generate header files via <code>javac -h</code>
 */
class PngWrapper {

	public String getVersion() {
		return getVersion0();
	}

	private native String getVersion0();

	public Dimensions getDimensions(String path) {
		return getDimensions0(path);
	}

	private native Dimensions getDimensions0(String path);

}
