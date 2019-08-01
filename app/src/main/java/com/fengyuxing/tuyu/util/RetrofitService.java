package com.fengyuxing.tuyu.util;

import com.fengyuxing.tuyu.R;

import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by dai on 2018/11/30.
 */

public interface RetrofitService {
    String YXAppKey = "f67db07e93d6aee78e910f8d4e669b6f";//云信appkey  //    兔语  f67db07e93d6aee78e910f8d4e669b6f



//    String YXAppKey = "19db87f76968ab24ba2cbbef5e6c6aea";//云信appkey  正式       测试 d8b750432253c6a4f4c27cbd7862eb26

    //    String Head = "https://www.tuerapp.com";//正式域名
//    String Head = "https://www.fengyugo.com";//测试域名
    String Head = HttpChannel.baseUrl;//正式域名
    //推荐关注
    String RECOMMENDFOLLOW = "/drama/user/findRandomRecommendUsers";

    //我的关注
    String FindFollow = "/tuyu/user/findFollow";
    //我的粉丝
    String FindFans = "/tuyu/user/findFans";
    //关注用户
    String FollowUser = "/tuyu/user/followUser";
    //取消关注用户
    String CancelFollowUser = "/tuyu/user/cancelFollowUser";
    //查询直播间背景图片
    String FindBackgroundImgPath = "/tuyu/room/findBackgroundImgPath";
    //修改直播间背景图片
    String EditRoomBGI = "/tuyu/room/editRoomBGI";
    //   接口说明：查询热门的直播间
    String FindHotRoom = "/tuyu/home/findHotRoom";
    //   接口说明：查询分类
    String FindClassify = "/tuyu/home/findClassify";
    //   进入直播间查询信息
    String FindRoomInfo = "/tuyu/room/findRoomInfo";
    //   查询缓存房间信息
    String FindCacheRoomInfo = "/tuyu/room/findCacheRoomInfo";

    //   进入直播间查询信息
    String FindRoomInfo2 = "/tuyu/room/findRoomInfo";
    //   收藏直播间
    String CollectRoom = "/tuyu/room/collectRoom";
    String FindRecommendRoom = "/tuyu/home/findRecommendRoom";

    //   查询收藏的直播间
    String FindCollectRoom = "/tuyu/home/findCollectRoom";

    //   取消收藏直播间
    String CancelCollectRoom = "/tuyu/room/cancelCollectRoom";
    //   查询用户信息
    String FindUserInfo = "/tuyu/user/findUserInfo";
    //   校验手机号
    String ValidatePhoneNumber = "/tuyu/login/validatePhoneNumber";

    //修改直播间信息
    String EditRoomInfo = "/tuyu/room/editRoomInfo";

    //心动值清零
    String ClearCharm = "/tuyu/room/clearCharm";

    //不展示心动值
    String CancelShowCharm = "/tuyu/room/cancelShowCharm";

    //展示心动值
    String ShowCharm = "/tuyu/room/showCharm";

    //创建房间
    String OpenMyRoom = "/tuyu/room/openMyRoom";
    //查询直播间观众
    String FindOnlineAudience = "/tuyu/roomList/findOnlineAudience";


    //连接心跳聊天室
    String ConnectRoom = "/tuyu/room/connectRoom";

    //查询直播间排麦人员
    String FindLiner = "/tuyu/roomList/findLiner";

    //查询直播间黑名单
    String FindBlacker = "/tuyu/roomList/findBlacker";

    //查询直播间管理员
    String FindManager = "/tuyu/roomList/findManager";

    //抱人上麦
    String PickUpForMike = "/tuyu/roomMike/pickUpForMike";
    //抱人上麦
    String PickUpForMike2 = "/roomMike/pickUpForMike?";
    //抱人下麦
    String PickDownForMike = "/tuyu/roomMike/pickDownForMike";
    //直播间点麦位排麦  直播间按麦位类型排麦
    String LineForMike = "/tuyu/roomMike/lineForMike";
    //闭麦操作
    String CloseMike = "/tuyu/roomMike/closeMike";
    //取消闭麦操作
    String CancelCloseMike = "/tuyu/roomMike/cancelCloseMike";
    //锁麦操作
    String LockMike = "/tuyu/roomMike/lockMike";
    //取消锁麦操作  cancelLockMike
    String CancelLockMike = "/tuyu/roomMike/cancelLockMike";
    //设置麦位排队操作
    String SetLineMike = "/tuyu/roomMike/setLineMike";
    //取消设置麦位排队操作
    String CancelSetLineMike = "/tuyu/roomMike/cancelSetLineMike";
    //设置老板位操作
    String SetBossMike = "/tuyu/roomMike/setBossMike";
    //取消设置老板位操作
    String CancelSetBossMike = "/tuyu/roomMike/cancelSetBossMike";
    //设置主持位
    String SetReceptionistMike = "/tuyu/roomMike/setReceptionistMike";
    //取消设置主持位
    String CancelSetReceptionistMike = "/tuyu/roomMike/cancelSetReceptionistMike";

    //查询礼物
    String FindGift = "/tuyu/gift/findGift";

    //查询背包礼物
    String FindPersonalGift = "/tuyu/gift/findPersonalGift";
    //退出房间
    String QuitRoom = "/tuyu/room/quitRoom";

    //直播间设置管理员
    String AddManager = "/tuyu/roomList/addManager";
    //直播间移除管理员
    String DeleteManager = "/tuyu/roomList/deleteManager";


    //直播间添加黑名单
    String AddBlacker = "/tuyu/roomList/addBlacker";
    //直播间移除黑名单
    String DeleteBlacker = "/tuyu/roomList/deleteBlacker";

    //查询首页排行榜数据
    String FindHomeRankingData = "/tuyu/ranking/findHomeRankingData";

    //查询房间排行榜数据
    String FindRoomRankingData = "/tuyu/ranking/findRoomRankingData";

    //查询排行榜前三名
    String FindRankingTop3 = "/tuyu/ranking/findRankingTop3";


    //查询首页banner
    String FindBanner = "/tuyu/home/findBanner";


    //添加禁言用户
    String AddForbider = "/tuyu/roomList/addForbider";
    //取消禁言用户
    String DeleteForbider = "/tuyu/roomList/deleteForbider";
    //修改直播间公告
    String EditRoomDescription = "/tuyu/room/editRoomDescription";

    //查询我收到的礼物
    String FindMyGiftLog = "/tuyu/gift/findMyGiftLog";
    //查询我的好友
    String FindFriend = "/tuyu/user/findFriend";

    //搜索房间
    String SearchRoom = "/tuyu/search/searchRoom";

    //搜索用户
    String SearchUser = "/tuyu/search/searchUser";


    //拉黑用户
    String DefriendUser = "/tuyu/user/defriendUser";

    //取消拉黑用户
    String CancelDefriendUser = "/tuyu/user/cancelDefriendUser";

    //查询我的黑名单
    String FindMyBlacker = "/tuyu/user/findBlacker";

    //修改用户信息
    String EditUserInfo = "/tuyu/user/editUserInfo";


    //打开公屏
    String ShowChat = "/tuyu/room/showChat";

    //关闭公屏
    String CancelShowChat = "/tuyu/room/cancelShowChat";

    //QQ登录
    String QQLogin = "/tuyu/login/qqLogin";

    //微信登录
    String WXLogin = "/tuyu/login/wxLogin";

    //初始化用户信息
    String InitUserInfo = "/tuyu/login/initUserInfo";


    //支出记录
    String FindDrawLog = "/tuyu/draw/findDrawLog";

    //兑换钻石
    String DrawDiamond = "/tuyu/draw/drawDiamond";
    //提现
    String DrawCash = "/tuyu/draw/drawCash";
    //查询版本信息
    String FindVersion = "/tuyu/version/findVersion";

    //查询直播间内用户信息
    String FindRoomUserInfo = "/tuyu/room/findRoomUserInfo";

    //查询是否可以提现  https://www.tuerapp.com/tuyu/draw/findCanDrawCash
    String FindCanDrawCash = "/tuyu/draw/findCanDrawCash";

    //发送提现验证码
    String SendValidateCode = "/tuyu/draw/sendValidateCode";

    //验证提现验证码
    String ValidatePhoneNumberTX = "/tuyu/draw/validatePhoneNumber";
    //查询文字合法
    String FindWordLegal = "/tuyu/validate/findWordLegal";

    //打开礼物动效
    String ShowGift = "/tuyu/room/showGift";

    //关闭礼物动效
    String CancelShowGift = "/tuyu/room/cancelShowGift";


    int[] bgAraay = {R.mipmap.label_levle_0, R.mipmap.label_levle_1, R.mipmap.label_levle_2, R.mipmap.label_levle_3, R.mipmap.label_levle_4, R.mipmap.label_levle_5, R.mipmap.label_levle_6,
            R.mipmap.label_levle_7, R.mipmap.label_levle_8, R.mipmap.label_levle_9, R.mipmap.label_levle_10, R.mipmap.label_levle_11, R.mipmap.label_levle_12, R.mipmap.label_levle_13,
            R.mipmap.label_levle_14, R.mipmap.label_levle_15, R.mipmap.label_levle_16, R.mipmap.label_levle_17, R.mipmap.label_levle_18, R.mipmap.label_levle_19, R.mipmap.label_levle_20,
            R.mipmap.label_levle_21, R.mipmap.label_levle_22, R.mipmap.label_levle_23, R.mipmap.label_levle_24, R.mipmap.label_levle_25, R.mipmap.label_levle_26, R.mipmap.label_levle_27,
            R.mipmap.label_levle_28, R.mipmap.label_levle_29, R.mipmap.label_levle_30, R.mipmap.label_levle_31, R.mipmap.label_levle_32, R.mipmap.label_levle_33, R.mipmap.label_levle_34,
            R.mipmap.label_levle_35, R.mipmap.label_levle_36, R.mipmap.label_levle_37, R.mipmap.label_levle_38, R.mipmap.label_levle_39, R.mipmap.label_levle_40, R.mipmap.label_levle_41,
            R.mipmap.label_levle_42, R.mipmap.label_levle_43, R.mipmap.label_levle_44, R.mipmap.label_levle_45, R.mipmap.label_levle_46, R.mipmap.label_levle_47, R.mipmap.label_levle_48,
            R.mipmap.label_levle_49, R.mipmap.label_levle_50, R.mipmap.label_levle_51, R.mipmap.label_levle_52, R.mipmap.label_levle_53, R.mipmap.label_levle_54, R.mipmap.label_levle_55,
            R.mipmap.label_levle_56, R.mipmap.label_levle_57, R.mipmap.label_levle_58, R.mipmap.label_levle_59, R.mipmap.label_levle_60, R.mipmap.label_levle_61, R.mipmap.label_levle_62,
            R.mipmap.label_levle_63, R.mipmap.label_levle_64, R.mipmap.label_levle_65, R.mipmap.label_levle_66, R.mipmap.label_levle_67, R.mipmap.label_levle_68, R.mipmap.label_levle_69,
            R.mipmap.label_levle_70, R.mipmap.label_levle_71, R.mipmap.label_levle_72, R.mipmap.label_levle_73, R.mipmap.label_levle_74, R.mipmap.label_levle_75, R.mipmap.label_levle_76,
            R.mipmap.label_levle_77, R.mipmap.label_levle_78, R.mipmap.label_levle_79, R.mipmap.label_levle_80, R.mipmap.label_levle_81, R.mipmap.label_levle_82, R.mipmap.label_levle_83,
            R.mipmap.label_levle_84, R.mipmap.label_levle_85, R.mipmap.label_levle_86, R.mipmap.label_levle_87, R.mipmap.label_levle_88, R.mipmap.label_levle_89, R.mipmap.label_levle_90,
            R.mipmap.label_levle_91, R.mipmap.label_levle_92, R.mipmap.label_levle_93, R.mipmap.label_levle_94, R.mipmap.label_levle_95, R.mipmap.label_levle_96, R.mipmap.label_levle_97,
            R.mipmap.label_levle_98, R.mipmap.label_levle_99, R.mipmap.label_levle_100};


    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap WeakHashMap<String, Object> weakHashMap);

    @Multipart
    @POST("/drama/user/editMyPortraitPath")
    Call<String> uploadFile(@Part("userId") String userId, @Part("key") String key, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Call<String> postFile(@Url String url, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Call<String> uploadFile(@Url String url, @Part MultipartBody.Part file, @PartMap Map<String, RequestBody> params);


}
