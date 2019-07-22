package com.fengyuxing.tuyu.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/13.
 */

public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "share_data";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        if (null != context) {

            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if (object instanceof String) {
                editor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                editor.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                editor.putLong(key, (Long) object);
            } else {
                editor.putString(key, object.toString());
            }

            SharedPreferencesCompat.apply(editor);
        }
    }

    //保存融云消息进行缓存
    public static <T> void setDataList(Context context, String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
    //    editor.clear();
        editor.putString(tag, strJson);
        SharedPreferencesCompat.apply(editor);
    }




    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经应对的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }




    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
            }
            editor.commit();
        }
    }


    /***************
     * Toast提示
     */
    public static void showToast(Context context, String showMsg) {
        Toast.makeText(context, showMsg, Toast.LENGTH_SHORT).show();
    }


    /******
     *
     * @param context
     * @param type 1点赞 2踩 3评论
     */
    public static void showToast(Context context, int type) {
//        Config config = CommonUtils.getConfig(context);
//        String score1 = config!=null && StringUtils.isNotEmpty(config.getSpare2()) ? config.getSpare2(): Config.score_zan_default;
//        String score2 = config!=null && StringUtils.isNotEmpty(config.getSpare3()) ? config.getSpare3(): Config.score_cai_default;
//        String score3 = config!=null && StringUtils.isNotEmpty(config.getSpare4()) ? config.getSpare4(): Config.score_comment_default;
//        score1 = "点赞成功，+"+score1+"积分";
//        score2 = "踩成功，+"+score2+"积分";
//        score3 = "评论成功，+"+score3+"积分";
//       String score4 = "删除成功，-"+score3+"积分";

        String score1 = "点赞成功，+1积分";
        String score2 = "踩成功，+1积分";
        String score3 = "评论成功，+2积分";
        String score4 = "删除成功，-2积分";
        if(type==1){
            Toast.makeText(context,score1, Toast.LENGTH_SHORT).show();
        }
//        else if(type==2){
//            Toast.makeText(context,score2, Toast.LENGTH_SHORT).show();
//        }
        else if(type==3){
            Toast.makeText(context,score3, Toast.LENGTH_SHORT).show();
        }else if(type==4){
            Toast.makeText(context,score4, Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 获取屏幕的宽
     *
     * @param context
     * @return
     */
    public static int getWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        return width;
    }


    /**
     * @param <T>
     * @param <T>
     * @param string json 字符串
     * @param T      转为对象
     * @return 对象
     * @Description Json字符串转为List 对象
     */
    public static <T> List<T> jsonStringToList(String string, Class<T> T) {

        try {
            Gson gson = new Gson();
            List<T> lst = new ArrayList<>();
            JsonArray array = new JsonParser().parse(string).getAsJsonArray();
            for (final JsonElement element : array) {
                lst.add(gson.fromJson(element, T));
            }
            return lst;
        } catch (Exception e) {
            return null;
        }

    }

}
