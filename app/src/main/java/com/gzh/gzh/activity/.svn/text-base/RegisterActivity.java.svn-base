package com.gzh.gzh.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.library.api.GZHApi;
import com.gzh.library.constant.Constants;
import com.gzh.library.constant.Event;
import com.gzh.library.util.CheckUtil;
import com.gzh.library.util.ToastUtil;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/7/19.
 * 注册页面
 */

public class RegisterActivity extends Base1Activity {

    private static final String TAG = "RegisterActivity";
    @Bind(R.id.ll_main)
    LinearLayout mLlMain;
    @Bind(R.id.et_name)
    EditText mEtName;
    @Bind(R.id.et_mobile)
    EditText mEtMobile;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    @Bind(R.id.tv_next)
    TextView mTvNext;
    @Bind(R.id.img_yes)
    ImageView mImgYes;
    @Bind(R.id.tv_sms)
    TextView mTvSms;
    private TimeCount time;
    private boolean mNameRight = false;
    private boolean mMobileRight = false;
    private boolean mCodeRight = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        Util.addLayoutListener(mLlMain, mTvNext);
        mTvSms.setText(Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, ""));
        CheckUtil.setNickname(mEtName);
        time = new TimeCount(60000, 1000);
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mNameRight = s.length() != 0;
                nextBack();
            }
        });
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
        rxManage.on(Event.IS_REGISTER, new Action1() {
            @Override
            public void call(Object o) {
                if (o.toString().equals("用户名存在")) {
                    ToastUtil.toast(RegisterActivity.this, getString(R.string.is_register));
                } else {
                    Intent intent = new Intent(RegisterActivity.this, NumPwdActivity.class);
                    intent.putExtra(NumPwdActivity.NAME, mEtName.getText().toString());
                    intent.putExtra(NumPwdActivity.MOBILE, mEtMobile.getText().toString());
                    intent.putExtra(NumPwdActivity.CODE, mEtCode.getText().toString());
                    startActivity(intent);
                }
            }
        });

        rxManage.on(Event.GET_AUTH_CODE, new Action1<String>() {
            @Override
            public void call(String o) {
                mEtCode.requestFocus();
                ToastUtil.toast(RegisterActivity.this, getString(R.string.toast_code));
                time.start();
            }
        });

        rxManage.on(Event.CHECK_AUTH_CODE, new Action1<String>() {
            @Override
            public void call(String o) {
                String signA = Constants.SALT_CIPHER + "loginAccount=" + (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                signA = Util.encrypt(signA);
                GZHApi.getInstance().isRegister(Util.getNowTime(), signA, (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString());
            }
        });
    }

    private void nextBack() {
        if (mNameRight && mMobileRight && mCodeRight)
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

    @OnClick({R.id.img_back, R.id.et_name, R.id.tv_next, R.id.tv_send, R.id.tv_privacy, R.id.ll_yes, R.id.ll_sms})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_name:
                mEtName.setCursorVisible(true);
                break;
            case R.id.tv_next:
                if (Util.isEmpty(mEtName.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.toast_name));
                    return;
                }
                if (!CheckUtil.isMobile(mEtMobile.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                if (mEtCode.getText().toString().length() != 4) {
                    ToastUtil.toast(this, getString(R.string.toast_right_code));
                    return;
                }
                if (mImgYes.getVisibility() == View.GONE) {
                    ToastUtil.toast(this, getString(R.string.toast_choose_yes));
                    return;
                }
                String sign = Constants.SALT_CIPHER + "mobile=" + (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + "&verifyCode=" +
                mEtCode.getText().toString() + Constants.SALT_CIPHER;
                sign = Util.encrypt(sign);
                GZHApi.getInstance().checkAuthCode(0, mEtCode.getText().toString(), (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString(), sign, Util.getNowTime());
                break;
            case R.id.tv_send:
                if (!CheckUtil.isMobile(mEtMobile.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.toast_mobile));
                    return;
                }
                String sign1 = Constants.SALT_CIPHER + "mobile=" + (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString() + "&submitTime=" + Util.getNowTime() + Constants.SALT_CIPHER;
                sign1 = Util.encrypt(sign1);
                GZHApi.getInstance().getAuthCode(0, (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + mEtMobile.getText().toString(), sign1, Util.getNowTime());
                break;
            case R.id.tv_privacy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            case R.id.ll_yes:
                InputMethodManager imm = (InputMethodManager) mEtName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(mEtName.getWindowToken(),0);
                }
                if (mImgYes.getVisibility() == View.VISIBLE)
                    mImgYes.setVisibility(View.GONE);
                else
                    mImgYes.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_sms:
                startActivityForResult(new Intent(this, CountryActivity.class), 13131);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 13131 && resultCode == 666) {
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
