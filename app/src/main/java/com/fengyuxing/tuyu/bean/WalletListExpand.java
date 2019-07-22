package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

//：搜索接口
public class WalletListExpand implements Serializable {

    private static final long serialVersionUID = 2437848784778095851L;


    private Integer id;//":1, //钱包地址主键ID
    private Integer memberId;//":409, //钱包地址所属用户ID
    private String name;//":"我的EOS钱包",//钱包名称
    private String addr;//":"0xc2d5ef1b5e6234c6bcbce87bb05d579c8e9d5720",//钱包地址
    private String createTime;//":"2018-09-01 17:33:42"//创建时间
    private String content;//

    private String memberName;//":"张无忌",
    private String avatar;//":"https://test-chain.oss-cn-beijing.aliyuncs.com/201808226 103958.jpg",
    private Integer memberAuth;//":0,
    private String replyMemberName;//":null,
    private String location;//":null,
    private Integer commentCount;//":2
    private String  checked;
    private String  memberAvatar;//":"https://test-chain.oss-cn-beijing.aliyuncs.com/user_0906175758705618.jpeg",
    private String  memberIntroduce;//":"人生如棋，落子无悔。",
    private Integer memberLevel;//":2,
    private Integer isAuth;//":1,
    private Integer isFollow;//":0
    private Integer period;//":1837,
    private String point;//":5.82,
    private String getNumber;//":null,
    private String useToken;//":null
    private String pointId;//":1,
    private String useNumber;//":100,
    private String pointEscape;//":1.5,
    private String isEscape;//":1

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getUseNumber() {
        return useNumber;
    }

    public void setUseNumber(String useNumber) {
        this.useNumber = useNumber;
    }

    public String getPointEscape() {
        return pointEscape;
    }

    public void setPointEscape(String pointEscape) {
        this.pointEscape = pointEscape;
    }

    public String getIsEscape() {
        return isEscape;
    }

    public void setIsEscape(String isEscape) {
        this.isEscape = isEscape;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getGetNumber() {
        return getNumber;
    }

    public void setGetNumber(String getNumber) {
        this.getNumber = getNumber;
    }

    public String getUseToken() {
        return useToken;
    }

    public void setUseToken(String useToken) {
        this.useToken = useToken;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public String getMemberIntroduce() {
        return memberIntroduce;
    }

    public void setMemberIntroduce(String memberIntroduce) {
        this.memberIntroduce = memberIntroduce;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public Integer getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getMemberAuth() {
        return memberAuth;
    }

    public void setMemberAuth(Integer memberAuth) {
        this.memberAuth = memberAuth;
    }

    public String getReplyMemberName() {
        return replyMemberName;
    }

    public void setReplyMemberName(String replyMemberName) {
        this.replyMemberName = replyMemberName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
