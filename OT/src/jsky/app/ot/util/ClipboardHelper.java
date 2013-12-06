/*
 * Contains FreeBongo Source Code -- This software contains some element of
 * the FreeBongo Source Code.
 *
 * FreeBongo Source Code, copyrighted 1996-1998 Marimba, Inc.
 * All Rights Reserved.
 *
 * Use, duplication, modification or distribution of this software is
 * prohibited except in accordance with the terms of the FreeBongo License.
 * A copy of the FreeBongo License may be obtained at www.freebongo.org.
 * This software is provided "AS IS" without any express or implied warranty
 * of any kind. See the FreeBongo License for specific language governing
 * rights and limitations. The Contributor is solely responsible for any use
 * of this software. Marimba, Inc. shall not be liable in connection with any
 * use or distribution of this software.
 */

package jsky.app.ot.util;

import java.awt.*;
import java.awt.datatransfer.*;

/**
 * Utility shortcuts for accessing the system clipboard.
 * This uses the standard Java APIs.
 * <P>
 * All FreeBongo-related applications should use this helper
 * class, to ensure proper access to the system clipboard
 * at all times.
 *
 * @author		Klaas Waslander
 */
public final class ClipboardHelper {
	private static Object  contents = null;
	
	private static ClipboardHelper  requestor = new ClipboardHelper();

	/** not to be instantiated */
	private ClipboardHelper() {
	}

	/**
	 * Set the clipboard contents.
	 */
	public static void setClipboard(Object arg) {
		// use JDK clipboard in 1.1 if it is a string
		if (arg instanceof String) {
			try {
				Toolkit  toolkit = Toolkit.getDefaultToolkit();
				Clipboard  cb = toolkit.getSystemClipboard();
				StringSelection  s = new StringSelection((String) arg);
				cb.setContents(s, s);
			} catch (Throwable t) {
				// we're in Communicator or something again....
			}
		} else {
			try {
				Toolkit  toolkit = Toolkit.getDefaultToolkit();
				Clipboard  cb = toolkit.getSystemClipboard();
				StringSelection  s = new StringSelection("");
				cb.setContents(s, s);
			} catch (Throwable t) {
				// we're in Communicator or something again....
			}
		}
		contents = arg;
	}

	/**
	 * Get the clipboard contents.
	 */
	public static Object getClipboard() {
		Object  result = contents;
		try {
			Clipboard  cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable  trans = cb.getContents(requestor);

			// get clipboard content
			try {
				Object  sysContent = trans.getTransferData(DataFlavor.stringFlavor);
				if (sysContent != null) {
					if (sysContent instanceof String) {
						String  str = (String)sysContent;
						// for empty string: take contents of ClipboardHelper
						if (str.trim().length() == 0) {
							result = contents;
						} else {
							result = str;
						}
					}
				}
			} catch (java.io.IOException e) {
				//e.printStackTrace();
				result = contents;
			} catch (UnsupportedFlavorException e) {
				//e.printStackTrace();
				result = contents;
			}
		} catch (Throwable t) {
			// we're in Communicator or something again....
		}
		return result;
	}
}
