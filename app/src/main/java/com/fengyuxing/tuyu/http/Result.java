/*******************************************************************************
 *
 * Copyright (c) Weaver Info Tech Co. Ltd
 *
 * Result
 *
 * app.backend.network.Result.java
 * TODO: File description or class description.
 *
 * @author: Administrator
 * @since:  2014-5-10
 * @version: 1.0.0
 *
 * @changeLogs:
 *     1.0.0: First created this class.
 *
 ******************************************************************************/
package com.fengyuxing.tuyu.http;

/**
 * 一个特殊的Model类
 *
 */
public class Result<T>  {

	public static final String STATUS_NONE = "NNNNNN";
	public static final String STATUS_OK = "000000";
	public static final String STATUS_FAIL = "FFFFFF";

	public static final int STATUS_CODE_NONE = -1;
	public static final int STATUS_CODE_OK = 0;
	public static final int STATUS_CODE_FAIL = -2;

	public static final String MESSAGE_NONE = "";
	public static final String MESSAGE_REQUESTFIAL = "数据请求失败";

	/** 返回数据的状态 */
    protected String mStatus;
    /** 返回数据的信息 */
    protected String mMessage;
    /** 结构化数据 */
    protected T mObject;

    /** 以数字表示的状态（如果不能以数字表示，则统一为-1） */
    protected int mStatusCode;

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String status) {
		mStatus = status;
	}

	public int getStatusCode() {
		return mStatusCode;
	}

	public void setStatusCode(int statusCode) {
		mStatusCode = statusCode;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String message) {
		mMessage = message;
	}

	public T getObject() {
		return mObject;
	}

	public void setObject(T object) {
		mObject = object;
	}

	public boolean isOK() {
		return mStatus.equals(STATUS_OK);
	}

	@Override
	public String toString() {
		return "Result{" +
				"mStatus='" + mStatus + '\'' +
				", mMessage='" + mMessage + '\'' +
				", mObject=" + mObject +
				", mStatusCode=" + mStatusCode +
				'}';
	}
}
