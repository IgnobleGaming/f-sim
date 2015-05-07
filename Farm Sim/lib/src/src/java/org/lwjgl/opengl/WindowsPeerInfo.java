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
import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;

/**
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 2618 $
 * $Id: WindowsPeerInfo.java 2618 2006-10-30 15:46:42 +0000 (ma, 30 okt 2006) elias_naur $
 */
abstract class WindowsPeerInfo extends PeerInfo {
	public WindowsPeerInfo() {
		super(createHandle());
	}
	private static native ByteBuffer createHandle();

	protected void choosePixelFormat(int origin_x, int origin_y, PixelFormat pixel_format, IntBuffer pixel_format_caps, boolean use_hdc_bpp, boolean support_window, boolean support_pbuffer, boolean double_buffered) throws LWJGLException {
		nChoosePixelFormat(getHandle(), origin_x, origin_y, pixel_format, pixel_format_caps, use_hdc_bpp, support_window, support_pbuffer, double_buffered);
	}
	private static native void nChoosePixelFormat(ByteBuffer peer_info_handle, int origin_x, int origin_y, PixelFormat pixel_format, IntBuffer pixel_format_caps, boolean use_hdc_bpp, boolean support_window, boolean support_pbuffer, boolean double_buffered) throws LWJGLException;

	public final long getHwnd() {
		return nGetHwnd(getHandle());
	}
	private static native long nGetHwnd(ByteBuffer handle);
}
