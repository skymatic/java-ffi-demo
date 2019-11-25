HEADERS = -I"${JAVA_HOME}/include" -I"${JAVA_HOME}/include/darwin" -I"/usr/local/include/libpng16"

########

all: check-env install

install:
	gcc -c -Wall -D_JNI_IMPLEMENTATION_ $(HEADERS) de_skymatic_ffi_jni_PngWrapper.c
	gcc -dynamiclib -current_version 1.0 -Wl -lpng16 -shared -o libPngWrapper.dylib de_skymatic_ffi_jni_PngWrapper.o

check-env:
	test -n "$(JAVA_HOME)"  # $$ENV
