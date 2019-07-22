package com.fengyuxing.tuyu.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest implements IHttpRequest {

	public int mId;
	public int mMethod;
	public String mPath;
	public Map<String, String> mHeaders;
	public Map<String, String> mParameters;
	public Map<String, String> mConfigurations;

	public HttpRequest(int id, int method, String path) {
		mId = id;
		mMethod = method;
		mPath = path;
		mHeaders = new HashMap<String, String>();
		mParameters = new HashMap<String, String>();
		mConfigurations = new HashMap<String, String>();
	}

	@Override
	public void setId(int id) {
		mId = id;
	}

	@Override
	public int getId() {
		return mId;
	}

	@Override
	public void setMethod(int method) {
		mMethod = method;
	}

	@Override
	public int getMethed() {
		return mMethod;
	}

	@Override
	public void setPath(String path) {
		mPath = path;
	}

	@Override
	public String getPath() {
		return mPath;
	}

	@Override
	public Map<String, String> getHeaders() {
		return mHeaders;
	}

	@Override
	public void addHeader(String key, String value) {
		mHeaders.put(key, value);
	}

	@Override
	public Map<String, String> getParameters() {
		return mParameters;
	}

	@Override
	public void add(String key, String value) {
		mParameters.put(key, value);
	}

	@Override
	public Map<String, String> getConfigs() {
		return mConfigurations;
	}

	@Override
	public void addConfig(String key, String value) {
		mConfigurations.put(key, value);
	}

	@Override
	public boolean enableCache() {
		return true;
	}

	@Override
	public boolean enableRetry() {
		return true;
	}
}
