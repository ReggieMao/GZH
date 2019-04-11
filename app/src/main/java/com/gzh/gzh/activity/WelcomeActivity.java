package com.gzh.gzh.activity;

import android.content.Intent;
import android.view.KeyEvent;

import com.gzh.gzh.R;
import com.gzh.gzh.application.MyApplication;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MaoLJ on 2018/8/15.
 * 启动页
 */

public class WelcomeActivity extends Base1Activity {

    private static final String TAG = "WelcomeActivity";
    Timer timer = new Timer();
    private int recLen = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        if (!this.isTaskRoot()) { // 判断当前activity是不是所在任务栈的根
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        Util.immersiveStatus(this, true);
        if (UserPreference.getInt(UserPreference.LANGUAGE, 1) == 1) {
            Util.setLanguage(MyApplication.getMyApplicationInstance(), false);
        } else {
            Util.setLanguage(MyApplication.getMyApplicationInstance(), true);
        }
        timer.schedule(task, 1000, 1000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    recLen --;
                    if (recLen == 0) {
                        timer.cancel();
                        jumpOtherPage();
                    }
                }
            });
        }
    };

    private void jumpOtherPage() {
        if (!Util.isEmpty(UserPreference.getString(UserPreference.SESSION_ID, ""))) {
            if (!Util.isEmpty(UserPreference.getString(UserPreference.GESTURE_PWD, ""))) {
                Intent intent = new Intent(this, GesturePwd0Activity.class);
                intent.putExtra(GesturePwd0Activity.FROM_WELCOME, true);
                startActivity(intent);
            } else
                startActivity(new Intent(this, MainActivity.class));
        } else
            startActivity(new Intent(this, StartActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

}
