package org.yaen.ark.web.home.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * 信任管理器
 * 
 * @author yanghong
 * @date 2015-11-17
 */
public class MyX509TrustManager implements X509TrustManager {

	// 检查客户端证书
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	// 检查服务器端证书
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}

	// 返回受信任的X509证书数组 （受信任何服务端证书）
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
