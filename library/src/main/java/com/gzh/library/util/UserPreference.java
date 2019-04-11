package com.gzh.library.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.gzh.library.base.BaseApp;

/**
 * Created by MaoLJ on 2018/7/18.
 * 本地保存
 */

public class UserPreference {

    public static final String SESSION_ID = "session_id";
    public static final String SECRET = "secret";
    public static final String GESTURE_PWD = "gesture_pwd";
    public static final String PWD_ERR_COUNT = "pwd_err_count";
    public static final String ACCOUNT = "account";
    public static final String LANGUAGE = "language";
    public static final String SHOULD_PWD = "should_pwd";
//    public static final String TRADE_UNREAD = "trade_unread";
    public static final String EXCHANGE = "exchange";
    public static final String ADD_COIN_COUNT = "add_coin_count";
    public static final String VERSION_CODE = "version_code";
    public static final String VERSION_LOG = "version_log";
    public static final String VERSION_URL = "version_url";
    public static final String COUNTRY_SMS = "country_sms";
    public static final String GZH_AMOUNT = "gzh_amount";

    public static String sp_name;

    private static SharedPreferences getSharePreferences() {
        return BaseApp.getAppContext().getSharedPreferences(sp_name, Context.MODE_PRIVATE);
    }

    public static void putInt(String key, int value) {
        getSharePreferences().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int value){
        return getSharePreferences().getInt(key,value);
    }

    public static void putString(String key, String value) {
        getSharePreferences().edit().putString(key, value).apply();
    }

    public static String getString(String key, String def) {
        return getSharePreferences().getString(key, def);
    }

}
