/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_lwjgl_WindowsSysImplementation */

#ifndef _Included_org_lwjgl_WindowsSysImplementation
#define _Included_org_lwjgl_WindowsSysImplementation
#ifdef __cplusplus
extern "C" {
#endif
#undef org_lwjgl_WindowsSysImplementation_JNI_VERSION
#define org_lwjgl_WindowsSysImplementation_JNI_VERSION 8L
/*
 * Class:     org_lwjgl_WindowsSysImplementation
 * Method:    getTime
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_WindowsSysImplementation_getTime
  (JNIEnv *, jobject);

/*
 * Class:     org_lwjgl_WindowsSysImplementation
 * Method:    alert
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_WindowsSysImplementation_alert
  (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     org_lwjgl_WindowsSysImplementation
 * Method:    getClipboard
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_lwjgl_WindowsSysImplementation_getClipboard
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
