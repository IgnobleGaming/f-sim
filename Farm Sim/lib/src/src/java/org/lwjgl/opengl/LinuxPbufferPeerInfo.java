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
package org.lwjgl.opengl;

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;

/**
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 2596 $
 * $Id: LinuxPbufferPeerInfo.java 2596 2006-10-23 20:14:45 +0000 (ma, 23 okt 2006) elias_naur $
 */
final class LinuxPbufferPeerInfo extends LinuxPeerInfo {
	public LinuxPbufferPeerInfo(int width, int height, PixelFormat pixel_format) throws LWJGLException {
		LinuxDisplay.lockAWT();
		try {
			GLContext.loadOpenGLLibrary();
			try {
				LinuxDisplay.incDisplay();
				try {
					nInitHandle(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), getHandle(), width, height, pixel_format);
				} catch (LWJGLException e) {
					LinuxDisplay.decDisplay();
					throw e;
				}
			} catch (LWJGLException e) {
				GLContext.unloadOpenGLLibrary();
				throw e;
			}
		} finally {
			LinuxDisplay.unlockAWT();
		}
	}
	private static native void nInitHandle(long display, int screen, ByteBuffer handle, int width, int height, PixelFormat pixel_format) throws LWJGLException;

	public void destroy() {
		LinuxDisplay.lockAWT();
		nDestroy(getHandle());
		LinuxDisplay.decDisplay();
		GLContext.unloadOpenGLLibrary();
		LinuxDisplay.unlockAWT();
	}
	private static native void nDestroy(ByteBuffer handle);

	protected void doLockAndInitHandle() throws LWJGLException {
		// NO-OP
	}

	protected void doUnlock() throws LWJGLException {
		// NO-OP
	}
}
