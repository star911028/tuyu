package com.fengyuxing.tuyu.zego;

import android.content.Context;
import android.content.res.Resources;

import com.fengyuxing.tuyu.R;

/**
 * App's Id and key utils.
 * <p>
 * <p>Copyright © 2017 Zego. All rights reserved.</p>
 *
 * @author realuei on 2017/7/11.
 */

public class AppSignKeyUtils {
    @SuppressWarnings("unused")

    /**
     * 请提前在即构管理控制台获取 appID 与 appSign
     * Demo 默认使用 UDP 模式，请填充该模式下的 AppID 与 appSign,其他模式不需要可不用填
     * AppID 填写样式示例：1234567890l
     * appSign 填写样式示例：{(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x03}
     **/

    static final private long RTMP_APP_ID =00 ;

    static final public long UDP_APP_ID = 1643346996;// 兔语  1643346996        兔耳 1098281512

    static final public long INTERNATIONAL_APP_ID =00 ;


    final static private byte[] appSign_rtmp = {};


//    final static private byte[] appSign_udp = {(byte)0x23,(byte)0x28,(byte)0x8a,(byte)0x22,(byte)0x7e,(byte)0x70,(byte)0x89,(byte)0xc9,(byte)0xd0,(byte)0x8e
//            ,(byte)0x7c,(byte)0xfa,(byte)0x13,(byte)0x56,(byte)0x48,(byte)0xf2 ,
//            (byte)0x74,(byte)0xd0,(byte)0xed,(byte)0x52,(byte)0xd7,(byte)0x59
//            ,(byte)0xf9,(byte)0xd5,(byte)0xdc,(byte)0xab,(byte)0xdd
//            ,(byte)0xdb,(byte)0x36,(byte)0x41,(byte)0x0b,(byte)0xef};


    final static private byte[] appSign_udp = {(byte)0x47,(byte)0xd2,(byte)0x83,(byte)0x88,(byte)0xd7,(byte)0x78,(byte)0x47,(byte)0x97,(byte)0x74,(byte)0x33
            ,(byte)0x03,(byte)0x78,(byte)0x64,(byte)0xe2,(byte)0x5c,(byte)0x88 ,
            (byte)0xde,(byte)0x6b,(byte)0x50,(byte)0x1f,(byte)0x3b,(byte)0x31
            ,(byte)0x29,(byte)0xca,(byte)0xe5,(byte)0x1d,(byte)0x7b
            ,(byte)0x2f,(byte)0x13,(byte)0xd7,(byte)0x0e,(byte)0x89};


    final static private byte[] appSign_international = {};


    static public boolean isInternationalProduct(long appId) {
        return appId == INTERNATIONAL_APP_ID;
    }

    static public boolean isUdpProduct(long appId) {
        return appId == UDP_APP_ID;
    }

    static public byte[] requestSignKey(long appId) {
        if (appId == UDP_APP_ID) {
            return appSign_udp;
        } else if (appId == INTERNATIONAL_APP_ID) {
            return appSign_international;
        } else if (appId == RTMP_APP_ID) {
            return appSign_rtmp;
        }
        return null;
    }

    static public String getAppTitle(long currentAppFlavor, Context context) {
        String appTitle;
        Resources resources = context.getResources();
        if (currentAppFlavor == 1) {   // International
            appTitle = resources.getString(R.string.zg_app_title, resources.getString(R.string.zg_text_app_flavor_intl));
        } else if (currentAppFlavor == 2) {    // Custom
            appTitle = resources.getString(R.string.zg_app_title, resources.getString(R.string.zg_text_app_flavor_customize));
        } else {   // UDP
            appTitle = resources.getString(R.string.zg_app_title, resources.getString(R.string.zg_text_app_flavor_china));
        }
        return appTitle;
    }


    static public String convertSignKey2String(byte[] appSign) {
        if (appSign != null) {
            StringBuilder buffer = new StringBuilder();
            for (int b : appSign) {
                buffer.append("0x").append(Integer.toHexString((b & 0x000000FF) | 0xFFFFFF00).substring(6)).append(",");
            }
            buffer.setLength(buffer.length() - 1);
            return buffer.toString();
        } else {
            return "";
        }
    }

    static public byte[] parseSignKeyFromString(String strAppSign) throws NumberFormatException {
        String[] keys = strAppSign.split(",");
        if (keys.length != 32) {
            throw new NumberFormatException("App Sign Illegal");
        }
        byte[] byteSignKey = new byte[32];
        for (int i = 0; i < 32; i++) {
            int data = Integer.valueOf(keys[i].trim().replace("0x", ""), 16);
            byteSignKey[i] = (byte) data;
        }
        return byteSignKey;
    }
}
