/* 
 * Copyright (c) 2002-2004 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
/**
 * $Id: org_lwjgl_Sys.c 2521 2006-07-15 19:45:36Z elias_naur $
 *
 * Windows system library.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision: 2521 $
 */

#include "Window.h"
#include "mmsystem.h"
#include "org_lwjgl_WindowsSysImplementation.h"
#include "common_tools.h"
#include <malloc.h>

JNIEXPORT jlong JNICALL Java_org_lwjgl_WindowsSysImplementation_getTime(JNIEnv * env, jobject ignored) {
	DWORD time;

	timeBeginPeriod(1);
	time = timeGetTime();
	timeEndPeriod(1);

	return time;
}

JNIEXPORT void JNICALL Java_org_lwjgl_WindowsSysImplementation_alert(JNIEnv * env, jobject ignored, jstring title, jstring message) {
	char * eMessageText = GetStringNativeChars(env, message);
	char * cTitleBarText = GetStringNativeChars(env, title);
	MessageBox(getCurrentHWND(), eMessageText, cTitleBarText, MB_OK | MB_TOPMOST);

	printfDebugJava(env, "*** Alert ***%s\n%s\n", cTitleBarText, eMessageText);
	
	free(eMessageText);
	free(cTitleBarText);
}

JNIEXPORT jstring JNICALL Java_org_lwjgl_WindowsSysImplementation_getClipboard
  (JNIEnv * env, jobject ignored)
{
	// Check to see if there's text available in the clipboard
	BOOL textAvailable = IsClipboardFormatAvailable(CF_TEXT);
	BOOL unicodeAvailable = IsClipboardFormatAvailable(CF_UNICODETEXT);
	void *clipboard_data;
	jstring ret;
	HANDLE hglb;
	const wchar_t * str;

	if (unicodeAvailable) {
		if (!OpenClipboard(NULL))
			return NULL;
		hglb = GetClipboardData(CF_UNICODETEXT);
		if (hglb == NULL) {
			CloseClipboard();
			return NULL;
		}
		clipboard_data = GlobalLock(hglb);
		if (clipboard_data == NULL) {
			CloseClipboard();
			return NULL;
		}
		str = (const wchar_t *)clipboard_data;
		ret = (*env)->NewString(env, str, wcslen(str));
	} else if (textAvailable) {
		if (!OpenClipboard(NULL))
			return NULL;
		hglb = GetClipboardData(CF_TEXT);
		if (hglb == NULL) {
			CloseClipboard();
			return NULL;
		}
		clipboard_data = GlobalLock(hglb);
		if (clipboard_data == NULL) {
			CloseClipboard();
			return NULL;
		}
		ret = NewStringNative(env, (const char *) clipboard_data);
	} else {
		return NULL;
	}
	GlobalUnlock(hglb);
	CloseClipboard();
	return ret;
}
