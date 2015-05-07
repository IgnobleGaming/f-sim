/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_lwjgl_opengl_LinuxDisplay */

#ifndef _Included_org_lwjgl_opengl_LinuxDisplay
#define _Included_org_lwjgl_opengl_LinuxDisplay
#ifdef __cplusplus
extern "C" {
#endif
#undef org_lwjgl_opengl_LinuxDisplay_GrabSuccess
#define org_lwjgl_opengl_LinuxDisplay_GrabSuccess 0L
#undef org_lwjgl_opengl_LinuxDisplay_AutoRepeatModeOff
#define org_lwjgl_opengl_LinuxDisplay_AutoRepeatModeOff 0L
#undef org_lwjgl_opengl_LinuxDisplay_AutoRepeatModeOn
#define org_lwjgl_opengl_LinuxDisplay_AutoRepeatModeOn 1L
#undef org_lwjgl_opengl_LinuxDisplay_AutoRepeatModeDefault
#define org_lwjgl_opengl_LinuxDisplay_AutoRepeatModeDefault 2L
#undef org_lwjgl_opengl_LinuxDisplay_None
#define org_lwjgl_opengl_LinuxDisplay_None 0L
#undef org_lwjgl_opengl_LinuxDisplay_FULLSCREEN_LEGACY
#define org_lwjgl_opengl_LinuxDisplay_FULLSCREEN_LEGACY 1L
#undef org_lwjgl_opengl_LinuxDisplay_FULLSCREEN_NETWM
#define org_lwjgl_opengl_LinuxDisplay_FULLSCREEN_NETWM 2L
#undef org_lwjgl_opengl_LinuxDisplay_WINDOWED
#define org_lwjgl_opengl_LinuxDisplay_WINDOWED 3L
/* Inaccessible static: current_window_mode */
#undef org_lwjgl_opengl_LinuxDisplay_XRANDR
#define org_lwjgl_opengl_LinuxDisplay_XRANDR 10L
#undef org_lwjgl_opengl_LinuxDisplay_XF86VIDMODE
#define org_lwjgl_opengl_LinuxDisplay_XF86VIDMODE 11L
#undef org_lwjgl_opengl_LinuxDisplay_NONE
#define org_lwjgl_opengl_LinuxDisplay_NONE 12L
/* Inaccessible static: current_awt_lock_owner */
/* Inaccessible static: awt_lock_count */
/* Inaccessible static: display */
/* Inaccessible static: current_window */
/* Inaccessible static: display_connection_usage_count */
/* Inaccessible static: class_00024org_00024lwjgl_00024opengl_00024LinuxDisplay */
/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetCurrentGammaRamp
 * Signature: (JI)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetCurrentGammaRamp
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nIsXrandrSupported
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nIsXrandrSupported
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nIsXF86VidModeSupported
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nIsXF86VidModeSupported
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nIsNetWMFullscreenSupported
 * Signature: (JI)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nIsNetWMFullscreenSupported
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nLockAWT
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nLockAWT
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nUnlockAWT
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nUnlockAWT
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    openDisplay
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_LinuxDisplay_openDisplay
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    closeDisplay
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_closeDisplay
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetDefaultScreen
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetDefaultScreen
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nUngrabKeyboard
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nUngrabKeyboard
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGrabKeyboard
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGrabKeyboard
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGrabPointer
 * Signature: (JJJ)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGrabPointer
  (JNIEnv *, jclass, jlong, jlong, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nSetViewPort
 * Signature: (JJI)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nSetViewPort
  (JNIEnv *, jclass, jlong, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nUngrabPointer
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nUngrabPointer
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nDefineCursor
 * Signature: (JJJ)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nDefineCursor
  (JNIEnv *, jclass, jlong, jlong, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nCreateWindow
 * Signature: (JILjava/nio/ByteBuffer;Lorg/lwjgl/opengl/DisplayMode;III)J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nCreateWindow
  (JNIEnv *, jclass, jlong, jint, jobject, jobject, jint, jint, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nDestroyWindow
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nDestroyWindow
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nSwitchDisplayMode
 * Signature: (JIILorg/lwjgl/opengl/DisplayMode;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nSwitchDisplayMode
  (JNIEnv *, jclass, jlong, jint, jint, jobject);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nInternAtom
 * Signature: (JLjava/lang/String;Z)J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nInternAtom
  (JNIEnv *, jclass, jlong, jstring, jboolean);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetGammaRampLength
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetGammaRampLength
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nSetGammaRamp
 * Signature: (JILjava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nSetGammaRamp
  (JNIEnv *, jclass, jlong, jint, jobject);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nConvertToNativeRamp
 * Signature: (Ljava/nio/FloatBuffer;II)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nConvertToNativeRamp
  (JNIEnv *, jclass, jobject, jint, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetCurrentXRandrMode
 * Signature: (JI)Lorg/lwjgl/opengl/DisplayMode;
 */
JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetCurrentXRandrMode
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nSetTitle
 * Signature: (JJLjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nSetTitle
  (JNIEnv *, jclass, jlong, jlong, jstring);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nReshape
 * Signature: (JJIIII)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nReshape
  (JNIEnv *, jclass, jlong, jlong, jint, jint, jint, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetAvailableDisplayModes
 * Signature: (JII)[Lorg/lwjgl/opengl/DisplayMode;
 */
JNIEXPORT jobjectArray JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetAvailableDisplayModes
  (JNIEnv *, jclass, jlong, jint, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetInputFocus
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetInputFocus
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nIconifyWindow
 * Signature: (JJI)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nIconifyWindow
  (JNIEnv *, jclass, jlong, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nSetRepeatMode
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nSetRepeatMode
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetNativeCursorCapabilities
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetNativeCursorCapabilities
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetMinCursorSize
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetMinCursorSize
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetMaxCursorSize
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetMaxCursorSize
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nCreateCursor
 * Signature: (JIIIIILjava/nio/IntBuffer;ILjava/nio/IntBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nCreateCursor
  (JNIEnv *, jclass, jlong, jint, jint, jint, jint, jint, jobject, jint, jobject, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nCreateBlankCursor
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nCreateBlankCursor
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nDestroyCursor
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nDestroyCursor
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nGetPbufferCapabilities
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetPbufferCapabilities
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     org_lwjgl_opengl_LinuxDisplay
 * Method:    nSetWindowIcon
 * Signature: (JJLjava/nio/ByteBuffer;III)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nSetWindowIcon
  (JNIEnv *, jclass, jlong, jlong, jobject, jint, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
