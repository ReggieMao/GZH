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
 * 登录页面
 */

public class LoginActivity extends Base1Activity {

    private static final String TAG = "LoginActivity";
    @Bind(R.id.ll_main)
    LinearLayout mLlMain;
    @Bind(R.id.et_mobile)
    EditText mEtMobile;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.tv_sms)
    TextView mTvSms;
    private TimeCount time;
    private boolean mMobileRight = false;
    private boolean mCodeRight = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        Util.addLayoutListener(mLlMain, mTvLogin);
        mTvSms.setText(Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, ""));
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
                DialogUtil.logoutDialog(LoginActivity.this, getString(R.string.go_to_reg), new DialogUtil.OnResultListener0() {
                    @Override
                    public void onOK() {
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    }
                });
            }
        });

        rxManage.on(Event.GET_AUTH_CODE1, new Action1<String>() {
            @Override
            public void call(String o) {
                mEtCode.requestFocus();
                ToastUtil.toast(LoginActivity.this, getString(R.string.toast_code));
                time.start();
            }
        });

        rxManage.on(Event.LOGIN, new Action1<Login>() {
            @Override
            public void call(Login o) {
                UserPreference.putString(UserPreference.SESSION_ID, o.getSessionId());
                UserPreference.putString(UserPreference.SECRET, o.getSecret());
                UserPreference.putString(UserPreference.ACCOUNT, mEtMobile.getText().toString());
                ToastUtil.toast(LoginActivity.this, getString(R.string.toast_login_success));
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    private void nextBack() {
        if (mMobileRight && mCodeRight)
            mTvLogin.setBackground(getResources().getDrawable(R.drawable.bg_round_text_green2));
        else
            mTvLogin.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray1));
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

    @OnClick({R.id.img_back, R.id.et_mobile, R.id.tv_login, R.id.tv_send, R.id.ll_sms})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_mobile:
                mEtMobile.setCursorVisible(true);
                break;
            case R.id.tv_login:
                if (!CheckUtil.isMobile(mEtMobile.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                if (mEtCode.getText().toString().length() != 4) {
                    ToastUtil.toast(this, getString(R.string.toast_right_code));
                    return;
                }
                String sign = Constants.SALT_CIPHER + "loginAccount=" + (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + "&verifyCode=" + mEtCode.getText().toString() + Constants.SALT_CIPHER;
                sign = Util.encrypt(sign);
                GZHApi.getInstance().userLogin((Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString(), mEtCode.getText().toString(), sign, Util.getNowTime());
                break;
            case R.id.tv_send:
                if (!CheckUtil.isMobile(mEtMobile.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                String sign1 = Constants.SALT_CIPHER + "mobile=" + (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                sign1 = Util.encrypt(sign1);
                GZHApi.getInstance().getAuthCode(1, (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString(), sign1, Util.getNowTime());
                break;
            case R.id.ll_sms:
                startActivityForResult(new Intent(this, CountryActivity.class), 12121);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12121 && resultCode == 666) {
            mTvSms.setText("+" + data.getIntExtra("code", 0));
            UserPreference.putString(UserPreference.COUNTRY_SMS, mTvSms.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }

}
