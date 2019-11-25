#include "de_skymatic_ffi_jni_PngWrapper.h"
#include <stdio.h>
#include <png.h>

/*
 * Class:     de_skymatic_ffi_jni_PngWrapper
 * Method:    getVersion0
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_de_skymatic_ffi_jni_PngWrapper_getVersion0(JNIEnv *env, jobject thisObj) {
  char *versionString = PNG_LIBPNG_VER_STRING;
  return (*env)->NewStringUTF(env, versionString);
}

/*
 * Class:     de_skymatic_ffi_jni_PngWrapper
 * Method:    getDimensions0
 * Signature: (Ljava/lang/String;)Lde/skymatic/ffi/common/Dimensions;
 */
JNIEXPORT jobject JNICALL Java_de_skymatic_ffi_jni_PngWrapper_getDimensions0(JNIEnv *env, jobject thisObj, jstring path) {
  const char *filename = (*env)->GetStringUTFChars(env, path, 0);
  FILE *file = fopen(filename, "r");
  (*env)->ReleaseStringUTFChars(env, path, filename);
  
  png_structp png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL);
  png_infop info_ptr = png_create_info_struct(png_ptr);
  png_init_io(png_ptr, file);
  png_read_info(png_ptr, info_ptr);
  
  png_uint_32 width, height;
  png_get_IHDR(png_ptr, info_ptr, &width, &height, NULL, NULL, NULL, NULL, NULL);
  
  fclose(file);
  
  JavaVM *vm = NULL;
	if ((*env)->GetJavaVM(env, &vm) == JNI_OK) {
    jclass cls = (*env)->FindClass(env, "de/skymatic/ffi/common/Dimensions");
    jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "(II)V");
    jobject object = (*env)->NewObject(env, cls, constructor, width, height);
    return object;
	} else {
    return NULL;
  }
}
