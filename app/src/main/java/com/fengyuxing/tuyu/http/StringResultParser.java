package com.fengyuxing.tuyu.http;


import com.fengyuxing.tuyu.util.L;

public class StringResultParser extends ResultParser<String> {

	@Override
	public String parseObject(String json) {
		L.i("StringResultParser", "json:"+json);
		return json;
	}
}
