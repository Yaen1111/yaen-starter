package org.yaen.starter.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * serialize and unserialize
 * 
 * @author Yaen 2016年5月13日下午2:59:20
 */
public class SerializeUtil {
	public static byte[] serialize(Object object) {
		if (object == null) {
			return null;
		}

		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}

	public static Object unserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}

		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			return null;
		}
	}
}
