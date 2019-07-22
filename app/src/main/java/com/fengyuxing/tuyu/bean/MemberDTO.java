package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

/**
 * 用户个人信息缓存
 * @author LGJ
 */
public class MemberDTO extends Object implements Serializable {

	private static final long serialVersionUID = 2437848784778095851L;

	private Long id;
	private String nickname;//昵称
	private String email;
	private String phone;//电话
	private Integer sex;//性别
	private Integer level;  //级别
	private String avatar;//头像
	private String province;//省
	private String city;//市
	private String district;
	private String address;//地址
	private String openId;//
	private String introduce;//介绍
	private Integer score;

	private Integer isAuth;//":0, //大咖认证

	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}


	//	private  RongTokenDTO token;
	public static Integer score0 = 100;//注册奖励
	public static Integer score1 = 10;//发布资讯奖励
	public static Integer score2 = 3;//评论奖励
	public static Integer score3 = 3;//顶奖励
	public static Integer score4 = 2;//踩奖励
	public static Integer score5 = 5;//预测成功奖励




	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;


	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
