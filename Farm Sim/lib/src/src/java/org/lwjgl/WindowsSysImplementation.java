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
package org.lwjgl;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;

/**
 * <p>
 * @author $Author: elias_naur $
 * @version $Revision: 2521 $
 * $Id: WindowsSysImplementation.java 2521 2006-07-15 19:45:36Z elias_naur $
 */
class WindowsSysImplementation extends DefaultSysImplementation {
	static {
		Sys.initialize();
	}
	
	public long getTimerResolution() {
		return 1000;
	}

	public native long getTime();

	public native void alert(String title, String message);

	public boolean openURL(final String url) {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction() {
				public Object run() throws Exception {
					Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
					return null;
				}
			});
			return true;
		} catch (PrivilegedActionException e) {
			LWJGLUtil.log("Failed to open url (" + url + "): " + e.getCause().getMessage());
			return false;
		}
	}

	public native String getClipboard();
}
