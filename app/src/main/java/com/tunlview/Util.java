package com.tunlview;

/**
 * Created by Administrator on 2017/10/26.
 */

public class Util {
    /**
     * 二个小时时间间的差值,必须保证二个时间都是"HH:MM:ss"的格式，返回字符型的分钟
     */
    public static boolean compareTwoHour(String starttime, String endtime, int num) {
        String[] kk = null;
        String[] jj = null;
        kk = starttime.split(":");// 开始时间
        jj = endtime.split(":");// 结束时间
        int kksecond = (Integer.parseInt(kk[0]) * 60 + Integer.parseInt(kk[1])) * 60 + Integer.parseInt(kk[2]);
        int jjsecond = (Integer.parseInt(jj[0]) * 60 + Integer.parseInt(jj[1])) * 60 + Integer.parseInt(jj[2]);
        if (jjsecond - kksecond >= 0 && jjsecond - kksecond <= num) {
            return true;
        }
        return false;
    }
}
