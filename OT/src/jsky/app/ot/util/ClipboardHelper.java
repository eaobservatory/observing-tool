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
