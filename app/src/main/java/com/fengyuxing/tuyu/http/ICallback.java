package com.fengyuxing.tuyu.http;

public interface ICallback<T> {
	public void onStart(int id);
	public void onSuccess(int id, T result);
	public void onError(int id, int errorCode);
}