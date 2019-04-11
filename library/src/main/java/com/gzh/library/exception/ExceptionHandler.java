package com.gzh.library.exception;

import android.content.Context;
import android.util.Log;

import com.gzh.library.R;
import com.gzh.library.base.RxManage;
import com.gzh.library.constant.Event;
import com.gzh.library.constant.GZHCode;
import com.gzh.library.constant.GZHStrings;
import com.gzh.library.util.ToastUtil;
import com.gzh.library.util.Util;

/**
 * Created by MaoLJ on 2018/7/18.
 * 对ApiException异常进行统一处理
 */

public class ExceptionHandler {

    private static final String TAG = "ExceptionHandler";
    /** 上一次发生异常处理的时间 */
    private static long mLastHandlerStartTime = 0L;
    /** 异常处理的触发间隔时间 */
    public static long TRIGER_INTERVAL = 5000L;

    public static void handleException(Context context, ApiException e) {

        Log.d(TAG, "code == " + e.getCode());
        mLastHandlerStartTime = System.currentTimeMillis();

        if (!Util.isNetworkOpen(context)) {
            ToastUtil.toast(context, context.getString(R.string.net_error));
            return;
        }
        switch (e.getCode()) {
            case GZHCode.PARSE_ERROR:
                ToastUtil.toast(context, context.getString(R.string.rob_logout));
                new RxManage().post(Event.RE_LOGIN, null);
                break;

            case GZHCode.EMPTY_DATA:
                ToastUtil.toast(context, GZHStrings.EMPTY_DATA);
                break;

            case GZHCode.BUSSINESS_EXCEPTION:
                if (e.getMessage().equals("账户未注册，请注册后登录"))
                    new RxManage().post(Event.NOT_REGISTER, null);
                else
                    ToastUtil.toast(context, e.getMessage());
                break;

            case GZHCode.OUTDATE_OF_SESSION:
                ToastUtil.toast(context, GZHStrings.OUTDATE_OF_SESSION);
                break;

            case GZHCode.FOURCE_OUT_OF_SESSION:
                ToastUtil.toast(context, GZHStrings.FOURCE_OUT_OF_SESSION);
                break;

            case GZHCode.SECURITY_ERROR:
                ToastUtil.toast(context, GZHStrings.SECURITY_ERROR);
                break;

            case GZHCode.AUTHORIZATION_FAILURE:
                ToastUtil.toast(context, GZHStrings.AUTHORIZATION_FAILURE);
                break;

            case GZHCode.SERVER_INTERNAL_ERROR:
                ToastUtil.toast(context, GZHStrings.SERVER_INTERNAL_ERROR);
                break;

            case GZHCode.NETWORD_ERROR:
                ToastUtil.toast(context, context.getString(R.string.net_error));
                break;

            default:
                ToastUtil.toast(context, GZHStrings.SYSTEM_MAINTAINING);
                break;
        }
    }

    public static long getmLastHandlerStartTime() {
        return mLastHandlerStartTime;
    }

}
