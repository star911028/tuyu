package com.fengyuxing.tuyu.http;

import java.util.Map;

public interface IHttpRequest {
	public static final int METHOD_NONE = 0;
	public static final int METHOD_GET = 1;
	public static final int METHOD_POST =2;
	public void setId(int id);
    public int getId();
    public void setMethod(int method);
    public int getMethed();
    public void setPath(String path);
    public String getPath();
    public Map<String, String> getParameters();
    public void add(String key, String value);
    public Map<String, String> getHeaders();
    public void addHeader(String key, String value);
    public Map<String, String> getConfigs();
    public void addConfig(String key, String value);
    public boolean enableCache();
    public boolean enableRetry();
}
