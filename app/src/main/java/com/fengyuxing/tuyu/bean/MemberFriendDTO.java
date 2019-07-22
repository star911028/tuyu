package com.fengyuxing.tuyu.bean;

import java.io.Serializable;

/**
 * 我关注的人
 * @author LGJ
 */
public class MemberFriendDTO implements Serializable {

	private static final long serialVersionUID = 2437848784778095851L;
	
	private Long memberId;// 用户
	private String memberName;
	private String memberAvatar;
	private Integer memberLevel; //外键关联用户等级
	private String memberIntroduce;
	private Integer memberAuth; //认证状态 1未认证 1已认证




	public Integer getMemberAuth() {
		return memberAuth;
	}

	public void setMemberAuth(Integer memberAuth) {
		this.memberAuth = memberAuth;
	}

	public String getMemberIntroduce() {
		return memberIntroduce;
	}
	public void setMemberIntroduce(String memberIntroduce) {
		this.memberIntroduce = memberIntroduce;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberAvatar() {
		return memberAvatar;
	}
	public void setMemberAvatar(String memberAvatar) {
		this.memberAvatar = memberAvatar;
	}
	public Integer getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(Integer memberLevel) {
		this.memberLevel = memberLevel;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
