/**
 * 
 */
package org.yaen.starter.common.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * file util
 * 
 * @author Yaen 2016年3月29日下午4:28:28
 */
public class FileUtil {

	/** 路径分隔符Linux */
	public static final String PATH_SEPERIATOR_LINUX = "/";

	/** 路径分隔符Linux */
	public static final String PATH_SEPERIATOR_WINDOWS = "\\";

	/** 文件后缀名分隔符 */
	public static final String SUFFIX_SPLITOR = ".";

	/**
	 * path to linux format
	 * 
	 * @param path
	 * @return
	 */
	public static String toLinuxPath(String path) {
		return StringUtil.replace(path, PATH_SEPERIATOR_WINDOWS, PATH_SEPERIATOR_LINUX);
	}

	/**
	 * get extention
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileExtention(String fileName) {
		if (StringUtil.isEmpty(fileName)) {
			return StringUtil.EMPTY;
		}
		int index = StringUtil.lastIndexOf(fileName, SUFFIX_SPLITOR);
		if (index < 0) {
			return StringUtil.EMPTY;
		}
		return StringUtil.substring(fileName, index + 1).toLowerCase();
	}

	/**
	 * get file hash
	 * 
	 * @param fileName
	 * @param algorithm
	 * @return
	 * @throws Exception
	 */
	public static String getFileHash(String fileName, String algorithm) throws Exception {

		byte[] buffer = new byte[1024];
		int numRead;

		MessageDigest md = MessageDigest.getInstance(algorithm);

		InputStream fis = new FileInputStream(fileName);

		do {
			numRead = fis.read(buffer);
			// 从文件读到buffer，最多装满buffer
			if (numRead > 0) {
				md.update(buffer, 0, numRead);
				// 用读到的字节进行MD5的计算，第二个参数是偏移量
			}
		} while (numRead != -1);

		fis.close();

		BigInteger bi = new BigInteger(1, md.digest());
		return bi.toString(16);
	}

	/**
	 * get all hashes
	 * 
	 * @param fileName
	 * @return
	 */
	public static Map<String, String> getFileHashs(String fileName) {
		Map<String, String> map = new HashMap<String, String>();

		Provider[] providers = Security.getProviders();
		for (Provider prov : providers) {
			Set<Service> services = prov.getServices();
			for (Service serv : services) {
				String algorithm = serv.getAlgorithm();
				try {
					map.put(algorithm, getFileHash(fileName, algorithm));
				} catch (Exception ex) {
					//ex.printStackTrace();
				}
			}
		}
		return map;
	}

	public static void main(String arg[]) {
		Map<String, String> map = getFileHashs("C:\\Users\\xl\\Desktop\\监控\\config_backup (no hash).bin");

		for (String key : map.keySet()) {
			System.out.print(map.get(key) + "  :  ");
			System.out.println(key);
		}
	}
}
