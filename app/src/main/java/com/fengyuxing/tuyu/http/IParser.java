package com.fengyuxing.tuyu.http;

import android.content.Context;

public interface IParser<T> {
	public T parse(String data, Context context);
}
