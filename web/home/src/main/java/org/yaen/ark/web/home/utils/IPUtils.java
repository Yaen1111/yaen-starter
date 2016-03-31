/*******************************************************************************
 * Copyright (c) 2015 河北宜农网络科技有限公司. 版权所有.
 *******************************************************************************/
package org.yaen.ark.web.home.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author bb-he 2015年10月21日
 */
public class IPUtils {

	public static final Logger logger = LoggerFactory.getLogger(IPUtils.class);

	/**
	 * 获取服务端IP地址
	 * 
	 * @author bb-he
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getIpV4HostAddress() {
		String serverIp = "";
		Enumeration en = null;
		try {
			en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface network = (NetworkInterface) en.nextElement();
				Enumeration enAddr = network.getInetAddresses();
				while (enAddr.hasMoreElements()) {
					InetAddress addr = (InetAddress) enAddr.nextElement();
					if (addr instanceof Inet4Address) {
						if (!"127.0.0.1".equals(addr.getHostAddress())) {
							serverIp = addr.getHostAddress();
						}
					}
				}
			}
		} catch (SocketException e1) {
			System.out.println("获取EnumerationIP出错:" + e1.getMessage());
			e1.printStackTrace();
		}
		logger.info("得到服务器的IPV4地址：{}", serverIp);
		return serverIp;
	}

	/**
	 * 获取客户端IP地址
	 * 
	 * @author bb-he
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ip = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ip != null && ip.length() > 15) { // "***.***.***.***".length() = 15
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		logger.info("获取客户端IP地址：{}", ip);
		return ip;
	}

	public static void main(String[] args) {
		IPUtils.getIpV4HostAddress();
	}
}
