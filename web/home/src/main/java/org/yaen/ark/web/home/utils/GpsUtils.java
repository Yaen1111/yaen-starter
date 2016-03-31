package org.yaen.ark.web.home.utils;

public class GpsUtils {

  public static final double x_pi = (3.14159265358979324 * 3000.0) / 180.0;

  // 高德转百度
  public static double[] gaoToBd(double gg_lat, double gg_lon) {

    double[] gaoToBd = new double[2];
    double x = gg_lon, y = gg_lat;

    double z = Math.sqrt((x * x) + (y * y)) + (0.00002 * Math.sin(y * x_pi));
    double theta = Math.atan2(y, x) + (0.000003 * Math.cos(x * x_pi));

    // lat
    gaoToBd[0] = (z * Math.sin(theta)) + 0.006;

    // long
    gaoToBd[1] = (z * Math.cos(theta)) + 0.0065;

    return gaoToBd;
  }

  // 百度转高德
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

  public static void main(String[] args) {

    double[] s = GpsUtils.gaoToBd(31.192179, 121.523579);
    // 31.198056746570984
    // 121.53010215818301
    System.out.println(s[0]);
    System.out.println(s[1]);

    double[] y = GpsUtils.bdToGao(31.198056746570984, 121.53010215818301);

    System.out.println(y[0]);
    System.out.println(y[1]);
  }
}
