package com.fengyuxing.tuyu.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Date;

public final class JsonUtils {

	public static boolean getBoolean(JSONObject jsonObject, String key) {
		boolean value = false;
		if (jsonObject.has(key)) {
			try {
				value = jsonObject.getBoolean(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static Object fromJsonToJava(JSONObject json, Class pojo) throws Exception {
		// 首先得到pojo所定义的字段
		Field[] fields = pojo.getDeclaredFields();
		// 根据传入的Class动态生成pojo对象
		Object obj = pojo.newInstance();
		for(Field field: fields){
			// 设置字段可访问（必须，否则报错）
			field.setAccessible(true);
			// 得到字段的属性名
			String name = field.getName();
			// 这一段的作用是如果字段在JSONObject中不存在会抛出异常，如果出异常，则跳过。
			try{
				json.get(name);
			}catch(Exception ex){
				continue;
			}
			if(json.get(name) != null && !"".equals(json.getString(name))){
				// 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
				if(field.getType().equals(Long.class) || field.getType().equals(long.class)){
					field.set(obj, Long.parseLong(json.getString(name)));
				}else if(field.getType().equals(String.class)){
					field.set(obj, json.getString(name));
				} else if(field.getType().equals(Double.class) || field.getType().equals(double.class)){
					field.set(obj, Double.parseDouble(json.getString(name)));
				} else if(field.getType().equals(Integer.class) || field.getType().equals(int.class)){
					field.set(obj, Integer.parseInt(json.getString(name)));
				} else if(field.getType().equals(Date.class)){
					field.set(obj, Date.parse(json.getString(name)));
				}else{
					continue;
				}
			}
		}
		return obj;
	}

	public static int getInt(JSONObject jsonObject, String key) {
		int value = 0;
		if (jsonObject.has(key)) {
			try {
				value = jsonObject.getInt(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static long getLong(JSONObject jsonObject, String key) {
		long value = 0L;
		if (jsonObject.has(key)) {
			try {
				value = jsonObject.getLong(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static double getDouble(JSONObject jsonObject, String key) {
		double value = 0.0;
		if (jsonObject.has(key)) {
			try {
				value = jsonObject.getDouble(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static String getString(JSONObject jsonObject, String key) {
		String value = "";
		if (jsonObject.has(key)) {
			try {
				value = jsonObject.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static JSONObject getJSONObject(JSONObject jsonObject, String key) {
		JSONObject value = null;
		if (jsonObject.has(key)) {
			try {
				value = jsonObject.getJSONObject(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static JSONArray getJSONArray(JSONObject jsonObject, String key) {
		JSONArray value = null;
		if (jsonObject.has(key)) {
			try {
				value = jsonObject.getJSONArray(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static JSONObject toJsonObject(String json) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static JSONArray toJsonArray(String json) {
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
}
