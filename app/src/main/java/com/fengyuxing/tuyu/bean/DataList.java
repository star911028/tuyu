package com.fengyuxing.tuyu.bean;

import java.io.Serializable;
import java.util.List;

public class DataList extends MyBaseModel implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;


    private String portraitPath;//;//"http://www.otctu.com/img/user/2_1557385420195.png",
    private String onlineCount;//;//33,
    private String roomId;//;//2,
    private String roomName;//;//"我们的缘分就从你的爆音开始...",
    private String classifyName;//;//"处对象"
    private String classifyId;//;//1,
    private String expRank;//;//0,
    private String gender;//;//"男",
    private String description;//;//"大家好，希望大家关注我了！",
    private String userId;//;//1,
    private String age;//;//19,
    private String username;//;//"谁的胖子"
    private String bg;
    private String imageId;
    private String imgPath;
    private boolean checked;

    private String newUser;//private ;//true,
    private String token;//private ;//private String  05FAC7A04617F1AAE93BF1C4377F48A6private String
    private String birthDay;// private ;//private String  2000-07-07private String  ,
    private String address;//private;//private String  中国private String  ,
    private String needExp;//private;//1245,
    private String followCount;//private;//1,
    private String friendCount;//private;//1,
    private String tuId;//private;//private String  52259221private String  ,
    private String fansCount;//private;//1,
    private String myRoomId;//private;//3,
    private String number;//private;//private String  535663private String  ,
    //    private String portraitPathArray;//private;//[
    private String diamond;//private;//0,
    private String earnings;//private;//12.5,
    private String exp;//private;//3,
    private String[] portraitPathArray;//
    private String phoneNumber;//;//private String 18571453917
    private String isProtect;//;//false   private String  +
    private String identityStatus;//;//0}}
    private Boolean isFollow = false;
    private String hosterName;//private String 谁的胖子private String ,
    private String welcomeWord;//private String 欢迎语private String ,
    private String hosterPortraitPath;//private String http://www.tuerapp.com/img/user/1_1557384225187.pngprivate String ,
    private String isBlacker;//false,
    private String showCharm;//true,
    private String isCollect;//false,
    private String isHoster;//true,
    private String isMiker;//true,
    private String richerId;//1,
    private String richerPortraitPath;//private String http://www.tuerapp.com/img/user/1_1557384225187.pngprivate String ,
    private String hosterId;//1,
    private String password;//null,
    private String richerName;//private String 谁的胖子private String ,
    private String surplusTime;//483,
    private String isManager;//false,
    private String roleType;
    private List<MikeArray> mikeArray;//
    private String giftId;//;// 1,
    private String giftName;//;// "我们",
    private String price;//;// 1
    private String mikerId;//1,
    private String charm;//0,
    private String isBoss;//false,
    private String mikeNumber;//1,
    private String mikerName;//private String 谁的胖子private String ,
    private String mikeId;//5,
    private String isReceptionist;//true,
    private String mikerGender;//private String 男private String ,
    private String mikerPortraitPath;//private String http://www.tuerapp.com/img/user/1_1557384225187.pngprivate String ,
    private String status;//0正常  1闭麦 2锁麦
    private String needLine;//;//false,
    private String type;// "h5",//h5/page
    private String title;// "h5页面标题",
    private String h5TurnPath;// "跳转h5的路径",
    private String androidTurnPath;// "android跳转的页面路径",
    private String iosTurnPath;// "ios跳转的页面路径"
    private String needPassword;//;//false,是否需要密码
    private String giftCount;//230
    private String  accid;//
    private String   yunXinToken;
    private String    isDeFriend;
    private String   coin;

    private String  content;//": "兑换100钻石",
    private String  createTime;//": "2018-05-20",
    private String  cutCoin;//": "-100元宝",
    private String  out_trade_no;//": "订单号：1516464651321"

    private String isEditGender;//是否修改性别

    private String  alAccount;//": "13971142272",
    private String alRealName;//": "朱博文

    public String getAlAccount() {
        return alAccount;
    }

    public void setAlAccount(String alAccount) {
        this.alAccount = alAccount;
    }

    public String getAlRealName() {
        return alRealName;
    }

    public void setAlRealName(String alRealName) {
        this.alRealName = alRealName;
    }

    public String getIsEditGender() {
        return isEditGender;
    }

    public void setIsEditGender(String isEditGender) {
        this.isEditGender = isEditGender;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCutCoin() {
        return cutCoin;
    }

    public void setCutCoin(String cutCoin) {
        this.cutCoin = cutCoin;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getIsDeFriend() {
        return isDeFriend;
    }

    public void setIsDeFriend(String isDeFriend) {
        this.isDeFriend = isDeFriend;
    }

    public String getYunXinToken() {
        return yunXinToken;
    }

    public void setYunXinToken(String yunXinToken) {
        this.yunXinToken = yunXinToken;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(String giftCount) {
        this.giftCount = giftCount;
    }

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getH5TurnPath() {
        return h5TurnPath;
    }

    public void setH5TurnPath(String h5TurnPath) {
        this.h5TurnPath = h5TurnPath;
    }

    public String getAndroidTurnPath() {
        return androidTurnPath;
    }

    public void setAndroidTurnPath(String androidTurnPath) {
        this.androidTurnPath = androidTurnPath;
    }

    public String getIosTurnPath() {
        return iosTurnPath;
    }

    public void setIosTurnPath(String iosTurnPath) {
        this.iosTurnPath = iosTurnPath;
    }

    public String getMikerId() {
        return mikerId;
    }

    public void setMikerId(String mikerId) {
        this.mikerId = mikerId;
    }

    public String getCharm() {
        return charm;
    }

    public void setCharm(String charm) {
        this.charm = charm;
    }

    public String getIsBoss() {
        return isBoss;
    }

    public void setIsBoss(String isBoss) {
        this.isBoss = isBoss;
    }

    public String getMikeNumber() {
        return mikeNumber;
    }

    public void setMikeNumber(String mikeNumber) {
        this.mikeNumber = mikeNumber;
    }

    public String getMikerName() {
        return mikerName;
    }

    public void setMikerName(String mikerName) {
        this.mikerName = mikerName;
    }

    public String getMikeId() {
        return mikeId;
    }

    public void setMikeId(String mikeId) {
        this.mikeId = mikeId;
    }

    public String getIsReceptionist() {
        return isReceptionist;
    }

    public void setIsReceptionist(String isReceptionist) {
        this.isReceptionist = isReceptionist;
    }

    public String getMikerGender() {
        return mikerGender;
    }

    public void setMikerGender(String mikerGender) {
        this.mikerGender = mikerGender;
    }

    public String getMikerPortraitPath() {
        return mikerPortraitPath;
    }

    public void setMikerPortraitPath(String mikerPortraitPath) {
        this.mikerPortraitPath = mikerPortraitPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNeedLine() {
        return needLine;
    }

    public void setNeedLine(String needLine) {
        this.needLine = needLine;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNewUser() {
        return newUser;
    }

    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeedExp() {
        return needExp;
    }

    public void setNeedExp(String needExp) {
        this.needExp = needExp;
    }

    public String getFollowCount() {
        return followCount;
    }

    public void setFollowCount(String followCount) {
        this.followCount = followCount;
    }

    public String getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }

    public String getTuId() {
        return tuId;
    }

    public void setTuId(String tuId) {
        this.tuId = tuId;
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }

    public String getMyRoomId() {
        return myRoomId;
    }

    public void setMyRoomId(String myRoomId) {
        this.myRoomId = myRoomId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String[] getPortraitPathArray() {
        return portraitPathArray;
    }

    public void setPortraitPathArray(String[] portraitPathArray) {
        this.portraitPathArray = portraitPathArray;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsProtect() {
        return isProtect;
    }

    public void setIsProtect(String isProtect) {
        this.isProtect = isProtect;
    }

    public String getIdentityStatus() {
        return identityStatus;
    }

    public void setIdentityStatus(String identityStatus) {
        this.identityStatus = identityStatus;
    }

    public Boolean getFollow() {
        return isFollow;
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
    }

    public String getHosterName() {
        return hosterName;
    }

    public void setHosterName(String hosterName) {
        this.hosterName = hosterName;
    }

    public String getWelcomeWord() {
        return welcomeWord;
    }

    public void setWelcomeWord(String welcomeWord) {
        this.welcomeWord = welcomeWord;
    }

    public String getHosterPortraitPath() {
        return hosterPortraitPath;
    }

    public void setHosterPortraitPath(String hosterPortraitPath) {
        this.hosterPortraitPath = hosterPortraitPath;
    }

    public String getIsBlacker() {
        return isBlacker;
    }

    public void setIsBlacker(String isBlacker) {
        this.isBlacker = isBlacker;
    }

    public String getShowCharm() {
        return showCharm;
    }

    public void setShowCharm(String showCharm) {
        this.showCharm = showCharm;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getIsHoster() {
        return isHoster;
    }

    public void setIsHoster(String isHoster) {
        this.isHoster = isHoster;
    }

    public String getIsMiker() {
        return isMiker;
    }

    public void setIsMiker(String isMiker) {
        this.isMiker = isMiker;
    }

    public String getRicherId() {
        return richerId;
    }

    public void setRicherId(String richerId) {
        this.richerId = richerId;
    }

    public String getRicherPortraitPath() {
        return richerPortraitPath;
    }

    public void setRicherPortraitPath(String richerPortraitPath) {
        this.richerPortraitPath = richerPortraitPath;
    }

    public String getHosterId() {
        return hosterId;
    }

    public void setHosterId(String hosterId) {
        this.hosterId = hosterId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRicherName() {
        return richerName;
    }

    public void setRicherName(String richerName) {
        this.richerName = richerName;
    }

    public String getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(String surplusTime) {
        this.surplusTime = surplusTime;
    }

    public String getIsManager() {
        return isManager;
    }

    public void setIsManager(String isManager) {
        this.isManager = isManager;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public List<MikeArray> getMikeArray() {
        return mikeArray;
    }

    public void setMikeArray(List<MikeArray> mikeArray) {
        this.mikeArray = mikeArray;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getExpRank() {
        return expRank;
    }

    public void setExpRank(String expRank) {
        this.expRank = expRank;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public String getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(String onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }
}
