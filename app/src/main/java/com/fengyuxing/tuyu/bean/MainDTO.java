package com.fengyuxing.tuyu.bean;

import java.io.Serializable;
import java.util.List;

public class MainDTO implements Serializable {

    private String newUser;//private ;//true,
    private String userId;//private ;//3,
    private String token;//private ;//private String  05FAC7A04617F1AAE93BF1C4377F48A6private String  
    private String birthDay;// private ;//private String  2000-07-07private String  ,
    private String expRank;//private;//0,
    private String address;//private;//private String  中国private String  ,
    private String needExp;//private;//1245,
    private String followCount;//private;//1,
    private String friendCount;//private;//1,
    private String gender;//private;//private String  男private String  ,
    private String description;//private;//private String  private String  ,
    private String tuId;//private;//private String  52259221private String  ,
    private String fansCount;//private;//1,
    private String myRoomId;//private;//3,
    private String roomId;//private;//1,
    private String roomName;//private;//private String  我们的缘分就从你的爆音开始...private String  ,
    private String portraitPath;//private;//private String  http://www.tuerapp.com/img/user/1_1557384225187.pngprivate String  ,
    private String number;//private;//private String  535663private String  ,
    //    private String portraitPathArray;//private;//[
    private String diamond;//private;//0,
    private String earnings;//private;//12.5,
    private String exp;//private;//3,
    private String age;//private;//18,
    private String classifyName;//private;//private String  娱乐private String  ,
    private String username;//private;//private String  Jokerprivate String
    private String[] portraitPathArray;//
    private String imgPath;//{private String code;//1,private String errorMsg;//private String private String ,private String data;//{private String imgPath;//private String http://www.tuerapp.com/img/user/900985_1558001238812.jpgprivate String }}
    private String phoneNumber;//;//private String 18571453917
    private String isProtect;//;//false   private String  +
    private String identityStatus;//;//0}}
    private Boolean isFollow = false;
    private String hosterName;//private String 谁的胖子private String ,
    private String onlineCount;//1,
    private String welcomeWord;//private String 欢迎语private String ,
    private String classifyId;//1,
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
    private String  roleType;// 0  房主  1 管理员  2 普通用户
    private List<MikeArray> mikeArray;//
    private List<DataList> array;//
    private String   count;
    private String   yunXinToken;
    private String  isBoss;//":false,
    private String  mikeNumber;//":4,
    private String  mikeId;//":16,
    private String  isReceptionist;//":false,
    private String  push;//":true,
    private String  status;//":0,
    private String  needLine;//":false
    private String      yunXinRoomId;
    private String       backgroundImgPath;
    private    List<String>  rankingArray;
    private MyRank myRank;
    private    List<String>  attachArray;
    private String    isForbid;
    private String   accid;//
    private List<String>  contributionTopArray;//贡献榜
    private List<String>  charmTopArray;//魅力榜
    private String   showChat;//公屏显示
    private String   isLogin;//是否登录过
    private String downLoadUrl;//": "",
    private String canUpdate;//": true,   //表示是否有新版本
    private String needUpdate;//": true,  //表示是否需要强制更新
    private String versionNumber;//": "1.0.0",
    private String content;//": "第一个版本"
    private String hosterCharm;//房主心动值
    private Boolean showGift;//是否打开礼物特效

    public Boolean getShowGift() {
        return showGift;
    }

    public void setShowGift(Boolean showGift) {
        this.showGift = showGift;
    }

    public String getHosterCharm() {
        return hosterCharm;
    }

    public void setHosterCharm(String hosterCharm) {
        this.hosterCharm = hosterCharm;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(String canUpdate) {
        this.canUpdate = canUpdate;
    }

    public String getNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(String needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getShowChat() {
        return showChat;
    }

    public void setShowChat(String showChat) {
        this.showChat = showChat;
    }

    public List<String> getContributionTopArray() {
        return contributionTopArray;
    }

    public void setContributionTopArray(List<String> contributionTopArray) {
        this.contributionTopArray = contributionTopArray;
    }

    public List<String> getCharmTopArray() {
        return charmTopArray;
    }

    public void setCharmTopArray(List<String> charmTopArray) {
        this.charmTopArray = charmTopArray;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getIsForbid() {
        return isForbid;
    }

    public void setIsForbid(String isForbid) {
        this.isForbid = isForbid;
    }

    public List<String> getAttachArray() {
        return attachArray;
    }

    public void setAttachArray(List<String> attachArray) {
        this.attachArray = attachArray;
    }

    public MyRank getMyRank() {
        return myRank;
    }

    public void setMyRank(MyRank myRank) {
        this.myRank = myRank;
    }


    public List<String> getRankingArray() {
        return rankingArray;
    }

    public void setRankingArray(List<String> rankingArray) {
        this.rankingArray = rankingArray;
    }

    public String getBackgroundImgPath() {
        return backgroundImgPath;
    }

    public void setBackgroundImgPath(String backgroundImgPath) {
        this.backgroundImgPath = backgroundImgPath;
    }

    public String getYunXinRoomId() {
        return yunXinRoomId;
    }

    public void setYunXinRoomId(String yunXinRoomId) {
        this.yunXinRoomId = yunXinRoomId;
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

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
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

    public String getYunXinToken() {
        return yunXinToken;
    }

    public void setYunXinToken(String yunXinToken) {
        this.yunXinToken = yunXinToken;
    }

    public List<DataList> getArray() {
        return array;
    }

    public void setArray(List<DataList> array) {
        this.array = array;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    public String getHosterName() {
        return hosterName;
    }

    public void setHosterName(String hosterName) {
        this.hosterName = hosterName;
    }

    public String getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(String onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getWelcomeWord() {
        return welcomeWord;
    }

    public void setWelcomeWord(String welcomeWord) {
        this.welcomeWord = welcomeWord;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
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

    public Boolean getFollow() {
        return isFollow;
    }

    public void setFollow(Boolean follow) {
        isFollow = follow;
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String[] getPortraitPathArray() {
        return portraitPathArray;
    }

    public void setPortraitPathArray(String[] portraitPathArray) {
        this.portraitPathArray = portraitPathArray;
    }

    public String getExpRank() {
        return expRank;
    }

    public void setExpRank(String expRank) {
        this.expRank = expRank;
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

    public String getPortraitPath() {
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath) {
        this.portraitPath = portraitPath;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

//    public String getPortraitPathArray() {
//        return portraitPathArray;
//    }
//
//    public void setPortraitPathArray(String portraitPathArray) {
//        this.portraitPathArray = portraitPathArray;
//    }

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }


    public String getNewUser() {
        return newUser;
    }

    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
