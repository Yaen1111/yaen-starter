package org.yaen.starter.common.util.utils;

/**
 * gps util for convert gps formats
 * 
 * @author Yaen 2016年5月11日下午1:53:33
 */
public class GpsUtil {

	public static final double x_pi = (3.14159265358979324 * 3000.0) / 180.0;

	/**
	 * convert gps format from baidu to gaode
	 * 
	 * @param gd_lat
	 * @param gd_lon
	 * @return [0]=lat, [1]=long
	 */
	public static double[] gaoToBd(double gd_lat, double gd_lon) {

		double[] gaoToBd = new double[2];
		double x = gd_lon, y = gd_lat;

		double z = Math.sqrt((x * x) + (y * y)) + (0.00002 * Math.sin(y * x_pi));
		double theta = Math.atan2(y, x) + (0.000003 * Math.cos(x * x_pi));

		// lat
		gaoToBd[0] = (z * Math.sin(theta)) + 0.006;

		// long
		gaoToBd[1] = (z * Math.cos(theta)) + 0.0065;

		return gaoToBd;
	}

	/**
	 * convert gps format from baidu to gaode
	 * 
	 * @param bd_lat
	 * @param bd_lon
	 * @return [0]=lat, [1]=long
	 */
	public static double[] bdToGao(double bd_lat, double bd_lon) {

		double[] bdToGao = new double[2];
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt((x * x) + (y * y)) - (0.00002 * Math.sin(y * x_pi));
		double theta = Math.atan2(y, x) - (0.000003 * Math.cos(x * x_pi));

		// lat
		bdToGao[0] = z * Math.sin(theta);

		// lon
		bdToGao[1] = z * Math.cos(theta);

		return bdToGao;
	}

}
