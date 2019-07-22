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

import android.content.Context;


import com.fengyuxing.tuyu.util.JsonUtils;
import com.fengyuxing.tuyu.util.L;

import org.json.JSONObject;

/**
 * 一个特殊的Model类
 *
 */
public abstract class ResultParser<T> implements IParser<Result<T>> {

	private static final String TAG = ResultParser.class.getSimpleName();

	@Override
	public Result<T> parse(String json, Context context) {
		L.i(TAG, "JSON:"+json);
		final Result<T> result = new Result<T>();
    	final JSONObject jsonObject = JsonUtils.toJsonObject(json.trim());
    	
    	if (jsonObject != null) {
    		final Integer status = JsonUtils.getInt(jsonObject, "code");
			String object = JsonUtils.getString(jsonObject, "result");
			String message = JsonUtils.getString(jsonObject, "message");
			if(status==1){//成功
				result.setStatus(Result.STATUS_OK);
				result.setStatusCode(Result.STATUS_CODE_OK);
				result.setMessage(message != null ? message : Result.MESSAGE_NONE);
				result.setObject(object != null ? parseObject(object) : null);
			}else if(status==0){//失败
				result.setStatus(Result.STATUS_FAIL);
				result.setStatusCode(Result.STATUS_CODE_FAIL);
				result.setMessage(message != null ? message : Result.MESSAGE_NONE);
				result.setObject(null);
			}else{
				result.setStatus(status+"");
				result.setStatusCode(Result.STATUS_CODE_NONE);
				result.setMessage(message != null ? message : Result.MESSAGE_NONE);
				result.setObject(null);
			}
    	} else {
    		// 空数据的情况
    		result.setStatus(Result.STATUS_NONE);
    		result.setStatusCode(Result.STATUS_CODE_NONE);
    		result.setMessage(Result.MESSAGE_NONE);
    		result.setObject(null);
    	}
    	return result;
	}

	public abstract T parseObject(String json);
}
