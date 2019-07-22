package com.fengyuxing.tuyu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SharePreferenceUtil {
	private SharedPreferences mSharedPreferences;

	public SharePreferenceUtil(Context context, String file) {
		mSharedPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE);
	}

	public void setStringValue(String key) {
		mSharedPreferences.edit().putString(key, "").commit();
	}

	public void setStringValue(String key, String value) {
		mSharedPreferences.edit().putString(key, value).commit();
	}

	public String getStringValue(String key) {
		return mSharedPreferences.getString(key, "");
	}

	public String getStringValue(String key, String defaultValue) {
		return mSharedPreferences.getString(key, defaultValue);
	}

	public void setBooleanValue(String key, Boolean value) {
		mSharedPreferences.edit().putBoolean(key, value).commit();
	}

	public boolean getBooleanValue(String key, Boolean defaultValue) {
		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	public void setLongValue(String key, long value) {
		mSharedPreferences.edit().putLong(key, value).commit();
	}

	public long getLongValue(String key) {
		return mSharedPreferences.getLong(key, 0L);
	}

	public void setIntValue(String key, int value) {
		mSharedPreferences.edit().putInt(key, value).commit();
	}

	public int getIntValue(String key) {
		return mSharedPreferences.getInt(key, 0);
	}

	public int getIntValue(String key, int defaultValue) {
		return mSharedPreferences.getInt(key, defaultValue);
	}

	public void remove(String name) {
		mSharedPreferences.edit().remove(name).commit();
	}

	/**
	 * clear
	 */
	public void clear() {
		mSharedPreferences.edit().clear().commit();
	}

	public String[] getLgoinInfoList() {
		String str = getStringValue("Lgoin_Phone_List");
		if (!TextUtils.isEmpty(str)) {
			String[] mlist = str.split(",");
			List<String> list = new LinkedList<String>();
			for (int i = 0; i < mlist.length; i++) {
				if (!list.contains(mlist[i])) {
					list.add(mlist[i]);
				}
			}
			if (list != null && list.size() > 0) {
				return list.toArray(new String[list.size()]);
			}
		}
		return null;
	}

	public void setLgoinInfoItem(String phone) {
		if (!TextUtils.isEmpty(phone)) {
			int isc = 0;
			String[] list = getLgoinInfoList();
			if (list != null && list.length > 0) {
				for (int i = 0; i < list.length; i++) {
					if (list[i].equals(phone)) {
						if (i != 0) {
							isc = 1;
						}
						break;
					}
				}
				if (isc == 1) {
					List<String> mlist = new ArrayList<String>();
					mlist.add(phone);
					for (int i = 0; i < list.length; i++) {
						if (!list[i].equals(phone)) {
							mlist.add(list[i]);
						}
					}
					remove("Lgoin_Phone_List");
					String newStr = "";
					for (int i = 0; i < mlist.size(); i++) {
						newStr += mlist.get(i) + ",";
					}
					setStringValue("Lgoin_Phone_List", newStr);
				} else if (isc == 0) {
					String str = phone + "," + getStringValue("Lgoin_Phone_List");
					setStringValue("Lgoin_Phone_List", str);
				}
			} else {
				String str = phone + "," + getStringValue("Lgoin_Phone_List");
				setStringValue("Lgoin_Phone_List", str);
			}
		}
	}
}
