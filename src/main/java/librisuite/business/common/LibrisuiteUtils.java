/*
 * (c) LibriCore
 * 
 * Created on Aug 17, 2004
 * 
 * LibrisuiteUtils.java
 */
package librisuite.business.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/06/13 07:38:40 $
 * @since 1.0
 */
public abstract class LibrisuiteUtils {
	
	/**
	 * Utility for making deep copies (vs. clone()'s shallow copies) of 
	 * objects. Objects are first serialized and then deserialized. Error
	 * checking is fairly minimal in this implementation. If an object is
	 * encountered that cannot be serialized (or that references an object
	 * that cannot be serialized) an error is printed to System.err and
	 * null is returned. Depending on your specific application, it might
	 * make more sense to have copy(...) re-throw the exception.
	 *
	 * A later version of this class includes some minor optimizations.
	 */
		/**
		 * Returns a copy of the object, or null if the object cannot
		 * be serialized.
		 */
		public static Object deepCopy(Object orig) {
			Object obj = null;
			try {
				// Write the object out to a byte array
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bos);
				out.writeObject(orig);
				out.flush();
				out.close();

				// Make an input stream from the byte array and read
				// a copy of the object back in.
				ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
				obj = in.readObject();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			catch(ClassNotFoundException cnfe) {
			}
			return obj;
		}

		public static String asNullableString(Object obj) {
			if (obj == null) {
				return null;
			}
			else {
				return obj.toString();
			}
		}
		
	public static Character characterFromXML(String s) {
		if (s.length() == 0) {
			return null;
		}
		else {
			return new Character(s.charAt(0));
		}
	}

	public static String stringFromXML(String s) {
		if (s.length() == 0) {
			return null;
		}
		else {
			return s;
		}
	}
	}



