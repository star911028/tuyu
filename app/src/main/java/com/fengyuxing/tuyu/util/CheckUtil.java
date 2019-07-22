package com.fengyuxing.tuyu.util;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/23 0023.
 */


public class CheckUtil {
    public static final String[] PHONE_PREFIX = new String[]{"130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "150", "151", "152",
            "153", "154", "155", "156", "157", "158", "159", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190", "191", "192", "193", "194", "195", "196", "197", "198", "199"};

    public static boolean checkLocation(String mdn) {
        return checkMDN(mdn, false);
    }

    public static boolean checkMDN(String mdn) {
        return checkMDN(mdn, true);
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(19[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 妫�鏌ユ墜鏈哄彿鐮佸悎娉曟��? *
     *
     * @param mdn
     * @return
     */
    public static boolean checkMDN(String mdn, boolean checkLen) {
        if (mdn == null || mdn.equals("")) {
            return false;
        }
        // modify by zhangyp 鍘绘�?��风爜鍓嶈竟鐨�?86
        if (mdn.startsWith("+86")) {
            mdn = mdn.substring(3);
        }
        if (checkLen && mdn.length() != 11) {
            return false;
        }
        boolean flag = false;
        String p = mdn.length() > 3 ? mdn.substring(0, 3) : mdn;
        for (int i = 0; i < PHONE_PREFIX.length; i++) {
            if (p.equals(PHONE_PREFIX[i])) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return false;
        }
        return true;
    }

    public static final char[] INVALID_CH_CN = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', ',', ';', ':', '!', '@', '/', '(', ')', '[', ']', '{', '}', '|', '#', '$', '%', '^',
            '&', '<', '>', '?', '\'', '+', '-', '*', '\\', '\"'};

    public static boolean checkCN(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        char[] cArray = str.toCharArray();
        for (int i = 0; i < cArray.length; i++) {
            for (int j = 0; j < INVALID_CH_CN.length; j++) {
                if (cArray[i] == INVALID_CH_CN[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 鏄惁涓烘柊鐗堟�? true 涓烘湁鏂扮増鏈�?鍚﹀垯锛�? *
     *
     * @param
     * @return
     */
    public static boolean versionCompare(String oldversion, String newversion) {
        if (oldversion == null || newversion == null) {
            return false;
        }
        String[] oldstr = oldversion.split("\\.");
        String[] newstr = newversion.split("\\.");

        int[] oldint = new int[oldstr.length];
        int[] newint = new int[newstr.length];

        try {
            for (int i = 0; i < oldstr.length; i++) {
                oldint[i] = Integer.valueOf(oldstr[i]);
            }
            for (int i = 0; i < newstr.length; i++) {
                newint[i] = Integer.valueOf(newstr[i]);
            }
        } catch (Exception e) {
        }

        // 鍙互绠�鍖栫殑閫昏�?
        int count = oldint.length > newint.length ? newint.length : oldint.length;
        for (int temp = 0; temp < count; temp++) {
            if (newint[temp] == oldint[temp]) {
                continue;
            } else if (newint[temp] > oldint[temp]) {
                return true;
            } else {
                return false;
            }
        }
        if (newint.length > oldint.length) {
            return true;
        }
        return false;
    }

    /**
     * 妫�娴嬮偖绠卞悎娉曟��
     *
     * @param email
     * @return
     */
    public static boolean checkEmailValid(String email) {
        if ((email == null) || (email.trim().length() == 0)) {
            return false;
        }
        String regEx = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(email.trim().toLowerCase());

        return m.find();
    }

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern
            .compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    static String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\:\\d{1,5}$";

    private static final Pattern IP_PORT = Pattern.compile(regex);

    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    public static boolean validateIpPort(final String input) {
        return IP_PORT.matcher(input).matches();
    }

    /**
     * @param mobile
     * @return
     */
    public static String formatMobileNumber(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "";
        }
        return mobile.replaceAll("[\\.\\-\\ ]", "").trim();
    }

    public static boolean isIncreaseOrDeclineOrEqual(String password) {
        // 首先将String形式的字符串，转换成数字
        int password_int = Integer.parseInt(password);
        // 然后用一个集合保存各个位数
        List<Integer> password_weishu = new ArrayList<Integer>();
        // 现在开始取出各个位数
        int password_weishu_last = password_int;
        if (password_int == 0) {
            return false;
        }
        while (password_weishu_last != 0) {
            // 取出最后一位
            int last_weishu = password_weishu_last % 10;
            // 将最后一位加入List中
            password_weishu.add(last_weishu);
            // 重新赋值password_weishu_last
            password_weishu_last /= 10;
        }
        // 现在来判断List中是否相同，是否递增，是否递减
        // 用一个List来装中间的差值
        List<Integer> chazhi = new ArrayList<Integer>();
        for (int i = 0; i < password_weishu.size(); i++) {
            // 计算后一个数和前一个数之间的差值
            if ((i + 1) < password_weishu.size()) {
                int houyige = password_weishu.get(i + 1);
                int qianyige = password_weishu.get(i);
                chazhi.add(houyige - qianyige);
            }
        }
        // 判断差值
        boolean isRight = true;
        String jieguo = "";
        String jieguo_bidui = "";
        for (int i = 0; i < chazhi.size(); i++) {
            jieguo = Math.abs(chazhi.get(i)) + jieguo;
            jieguo_bidui = jieguo_bidui + "1";
        }
        if (Integer.parseInt(jieguo) == 0) {// 全部想等
            return false;
        }
        if (jieguo.equals(jieguo_bidui)) {// 全是1，标示递增，或者递减
            if (chazhi.get(0) > 0) {
                return false;// 标示递减

            }
            if (chazhi.get(0) < 0) {
                // 标示递增
                return false;
            }
        }
        return true;
    }

    /**
     * 根据毫秒返回时分秒
     *
     * @param time
     * @return
     */
    public static String getFormatHMS(long time) {
        time = time / 1000;//总秒数
        int s = (int) (time % 60);//秒
        int m = (int) (time / 60);//分
        int h = (int) (time / 3600);//秒
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
