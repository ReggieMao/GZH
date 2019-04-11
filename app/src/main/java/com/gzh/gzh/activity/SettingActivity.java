package com.gzh.gzh.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.gzh.application.MyApplication;
import com.gzh.library.api.GZHApi;
import com.gzh.library.constant.Event;
import com.gzh.library.pojo.RealmNotification;
import com.gzh.library.util.DialogUtil;
import com.gzh.library.util.FileUtil;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/7/24.
 * 设置页面
 */

public class SettingActivity extends Base1Activity {

    private static final String TAG = "SettingActivity";
    @Bind(R.id.tv_language)
    TextView mTvLanguage;
    private Realm mRealm = Realm.getDefaultInstance();

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        if (UserPreference.getInt(UserPreference.LANGUAGE, 1) == 1)
            mTvLanguage.setText(R.string.chinese);
        else
            mTvLanguage.setText(R.string.english);
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.LOGOUT, new Action1() {
            @Override
            public void call(Object o) {
                //清除本地头像文件
                if (FileUtil.fileIsExists(SettingActivity.this)) {
                    FileUtil.deleteFiles(new File(FileUtil.getUploadPath(SettingActivity.this) + "avatar.jpg"));
                }
                //清除到账通知数据
                RealmResults<RealmNotification> results = mRealm.where(RealmNotification.class).findAll();
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        results.deleteAllFromRealm();
                    }
                });
                UserPreference.putString(UserPreference.GESTURE_PWD, "");
                UserPreference.putString(UserPreference.SESSION_ID, "");
                startActivity(new Intent(SettingActivity.this, StartActivity.class));
            }
        });
    }

    @OnClick({R.id.img_back, R.id.ll_gesture, R.id.ll_language, R.id.ll_password, R.id.tv_logout})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_gesture:
                if (!Util.isEmpty(UserPreference.getString(UserPreference.GESTURE_PWD, "")))
                    startActivity(new Intent(this, GesturePwd0Activity.class));
                else {
                    Intent intent = new Intent(this, GesturePwd1Activity.class);
                    intent.putExtra(GesturePwd1Activity.FROM_SETTING, true);
                    startActivity(intent);
                }
                break;
            case R.id.ll_language:
                DialogUtil.languageDialog(this, UserPreference.getInt(UserPreference.LANGUAGE, 1), new DialogUtil.OnResultListener4() {
                    @Override
                    public void select1() {
                        mTvLanguage.setText(R.string.chinese);
                        Util.setLanguage(MyApplication.getMyApplicationInstance(), false);
                        UserPreference.putInt(UserPreference.LANGUAGE, 1);
                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                    }

                    @Override
                    public void select2() {
                        mTvLanguage.setText(R.string.english);
                        Util.setLanguage(MyApplication.getMyApplicationInstance(), true);
                        UserPreference.putInt(UserPreference.LANGUAGE, 2);
                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                    }
                });
                break;
            case R.id.ll_password:
                startActivity(new Intent(this, ChangePwd1Activity.class));
                break;
            case R.id.tv_logout:
                DialogUtil.logoutDialog(this, getString(R.string.logout_sure), new DialogUtil.OnResultListener0() {
                    @Override
                    public void onOK() {
                        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                        sign = Util.encrypt(sign);
                        GZHApi.getInstance().logout(Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""));
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

}
