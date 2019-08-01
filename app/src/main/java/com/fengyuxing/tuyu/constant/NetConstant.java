package com.fengyuxing.tuyu.constant;

import com.fengyuxing.tuyu.util.HttpChannel;

/**
 * Created by Administrator on 2017/10/9.
 */
public class NetConstant {
//public static final String HOST = "https://www.tuerapp.com/tuer";//正式服务器
//    public static final String HOST = "https://www.fengyugo.com/tuer";//测试服务器

    public static final String HOST = HttpChannel.baseUrl+"/tuyu";//兔语服务器

//    https://www.tuerapp.com/tuer/login/sendValidateCode
    public static final String V = "1.0";
    public static final String C = "1";
//    public static final String END = "?v=" + V + "&channel=" + C;

    public static final String API_SendCode = HOST + "/login/sendValidateCode?";//   接口说明：验证码发送接口
    public static final String API_ValidatePhoneNumber = HOST + "/login/validatePhoneNumber?";//   接口说明：校验手机号
    public static final String API_InitUserInfo = HOST + "/login/initUserInfo?";//   接口说明：初始化用户信息
    public static final String API_UploadIdImage = HOST + "/login/uploadIdImage?";//   接口说明：初始化用户信息
    public static final String API_FindHotRoom= HOST + "/home/findHotRoom?";//   接口说明：查询热门的直播间
    public static final String API_FindClassify= HOST + "/home/findClassify?";//   接口说明：查询分类
    public static final String API_FindRecentRoom= HOST + "/home/findRecentRoom?";//   接口说明：查询最近访问的直播间
    public static final String API_FindRecommendRoom= HOST + "/home/findRecommendRoom?";//   接口说明：查询收藏的直播间
    public static final String API_FindClassifyRoom= HOST + "/home/findClassifyRoom?";//   接口说明：查询分类的直播间
    public static final String API_FindUserInfo= HOST + "/user/findUserInfo?";//   接口说明：查询用户信息
    public static final String API_EditUserInfo= HOST + "/user/editUserInfo?";//   接口说明：修改用户信息
    public static final String API_UploadUserImg= HOST + "/user/uploadUserImg?";//   接口说明：上传用户图片
    public static final String API_FindSafeInfo= HOST + "/user/findSafeInfo?";//   接口说明：查询用户安全信息
    public static final String API_ValidateIdentity= HOST + "/login/validateIdentity?";//   接口说明：校验身份证号

    public static final String API_FindManager= HOST + "/roomList/findManager?";//   接口说明：查询房间管理员
    public static final String API_FindBlacker= HOST + "/roomList/findBlacker?";//   接口说明：查询房间黑名单
    public static final String API_DeleteBlacker= HOST + "/roomList/deleteBlacker?";//   接口说明：移除直播间黑名单
    public static final String API_DeleteManager= HOST + "/roomList/deleteManager?";//   接口说明：移除直播间管理员

    public static final String API_PickUpForMike= HOST + "/roomMike/pickUpForMike?";//
    public static final String API_findLiner= HOST + "/roomList/findLiner?";//   接口说明：查询直播间排麦人员
    public static final String API_LineForMike= HOST + "/roomMike/lineForMike?";//   接口说明：直播间按麦位类型排麦
    public static final String API_ClearLiner= HOST + "/roomList/clearLiner?";//   接口说明：清空直播间排麦人员
    public static final String API_CancelLineForMike= HOST + "/roomMike/cancelLineForMike?";//   接口说明：直播间取消排麦功能

    public static final String API_SendGift= HOST + "/gift/sendGift?";//   接口说明：赠送礼物
    public static final String API_SendPersonGift= HOST + "/gift/sendPersonGift?";//   接口说明：赠送背包礼物
    public static final String API_EditRoomBGI= HOST + "/room/editRoomBGI?";//   接口说明：修改直播间背景图片
    public static final String API_FollowUser= HOST + "/user/followUser?";//   接口说明：关注好友
    public static final String API_CancelFollowUser= HOST + "/user/cancelFollowUser?";//   接口说明：取消关注好友

    public static final String API_FindPersonalGift= HOST + "/gift/findPersonalGift?";//查询背包礼物

}