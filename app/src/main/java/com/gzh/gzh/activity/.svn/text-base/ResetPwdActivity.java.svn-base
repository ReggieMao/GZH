package com.gzh.gzh.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.library.api.GZHApi;
import com.gzh.library.constant.Constants;
import com.gzh.library.constant.Event;
import com.gzh.library.pojo.Login;
import com.gzh.library.util.ActivityUtil;
import com.gzh.library.util.CheckUtil;
import com.gzh.library.util.DialogUtil;
import com.gzh.library.util.ToastUtil;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/7/27.
 * 重置密码页面
 */

public class ResetPwdActivity extends Base1Activity {

    private static final String TAG = "ResetPwdActivity";
    @Bind(R.id.ll_main)
    LinearLayout mLlMain;
    @Bind(R.id.et_mobile)
    EditText mEtMobile;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    @Bind(R.id.tv_next)
    TextView mTvNext;
    private TimeCount time;
    private boolean mMobileRight = false;
    private boolean mCodeRight = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        ActivityUtil.add(this);
        Util.addLayoutListener(mLlMain, mTvNext);
        time = new TimeCount(60000, 1000);
        mEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    mMobileRight = true;
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_green3));
                } else {
                    mMobileRight = false;
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray4));
                }
                nextBack();
            }
        });
        mEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCodeRight = s.length() == 4;
                nextBack();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.NOT_REGISTER, new Action1() {
            @Override
            public void call(Object o) {
                DialogUtil.logoutDialog(ResetPwdActivity.this, getString(R.string.go_to_reg), new DialogUtil.OnResultListener0() {
                    @Override
                    public void onOK() {
                        startActivity(new Intent(ResetPwdActivity.this, RegisterActivity.class));
                    }
                });
            }
        });

        rxManage.on(Event.GET_AUTH_CODE2, new Action1<String>() {
            @Override
            public void call(String o) {
                ToastUtil.toast(ResetPwdActivity.this, getString(R.string.toast_code));
                time.start();
            }
        });

        rxManage.on(Event.CHECK_AUTH_CODE1, new Action1<String>() {
            @Override
            public void call(String o) {
                Intent intent = new Intent(ResetPwdActivity.this, ChangePwd2Activity.class);
                intent.putExtra(ChangePwd2Activity.FROM_FORGET, 1);
                intent.putExtra(ChangePwd2Activity.CODE, mEtCode.getText().toString());
                intent.putExtra(ChangePwd2Activity.COUNT, mEtMobile.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void nextBack() {
        if (mMobileRight && mCodeRight)
            mTvNext.setBackground(getResources().getDrawable(R.drawable.bg_round_text_green2));
        else
            mTvNext.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray1));
    }

    // 倒计时任务
    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTvSend.setClickable(false);
            mTvSend.setText(millisUntilFinished / 1000 + "s");
            mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray4));
        }

        @Override
        public void onFinish() {
            mTvSend.setText(R.string.send);
            mTvSend.setClickable(true);
            mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_round_text_green3));
        }
    }

    @OnClick({R.id.img_back, R.id.et_mobile, R.id.tv_next, R.id.tv_send})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_mobile:
                mEtMobile.setCursorVisible(true);
                break;
            case R.id.tv_next:
                if (!CheckUtil.isMobile(mEtMobile.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                if (mEtCode.getText().toString().length() != 4) {
                    ToastUtil.toast(this, getString(R.string.toast_right_code));
                    return;
                }
                String sign = Constants.SALT_CIPHER + "mobile=" + (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + "&verifyCode=" +
                        mEtCode.getText().toString() + Constants.SALT_CIPHER;
                sign = Util.encrypt(sign);
                GZHApi.getInstance().checkAuthCode(1, mEtCode.getText().toString(), (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString(), sign, Util.getNowTime());
                break;
            case R.id.tv_send:
                if (!CheckUtil.isMobile(mEtMobile.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                String sign1 = Constants.SALT_CIPHER + "mobile=" + (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                sign1 = Util.encrypt(sign1);
                GZHApi.getInstance().getAuthCode(2, (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString(), sign1, Util.getNowTime());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }

}
