package com.gzh.gzh.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.library.api.GZHApi;
import com.gzh.library.constant.Constants;
import com.gzh.library.constant.Event;
import com.gzh.library.util.ActivityUtil;
import com.gzh.library.util.ToastUtil;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/7/19.
 * 修改密码页面2
 */

public class ChangePwd3Activity extends Base1Activity {

    private static final String TAG = "ChangePwd3Activity";
    public static String FROM_FORGET = "from_forget";
    public static String OLD_PWD = "old_pwd";
    public static String NEW_PWD = "new_pwd";
    public static String CODE = "code";
    public static String COUNT = "count";
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.tv_sure)
    TextView mTvSure;
    private boolean mOldRight = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_pwd3;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        ActivityUtil.add(this);
        mEtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mOldRight = s.length() == 6;
                nextBack();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.UPDATE_PWD, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(ChangePwd3Activity.this, getString(R.string.pwd_sure1));
                ActivityUtil.finishAll();
            }
        });

        rxManage.on(Event.RESET_PWD, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(ChangePwd3Activity.this, getString(R.string.pwd_sure1));
                ActivityUtil.finishAll();
            }
        });
    }

    private void nextBack() {
        if (mOldRight)
            mTvSure.setBackground(getResources().getDrawable(R.drawable.bg_round_text_green2));
        else
            mTvSure.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray1));
    }

    @OnClick({R.id.img_back, R.id.tv_sure})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_sure:
                if (mEtPwd.getText().length() != 6) {
                    ToastUtil.toast(this, getString(R.string.pwd_tip3));
                    return;
                }
                if (!mEtPwd.getText().toString().equals(getIntent().getStringExtra(NEW_PWD))) {
                    ToastUtil.toast(this, getString(R.string.toast_sure_pwd));
                    return;
                }
                if (getIntent().getIntExtra(FROM_FORGET, 0) == 0) {
                    String sign = UserPreference.getString(UserPreference.SECRET, "") + "oldPassword=" + getIntent().getStringExtra(OLD_PWD) + "&password=" +
                            mEtPwd.getText().toString() + "&pwdType=2&submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                    sign = Util.encrypt(sign);
                    GZHApi.getInstance().updateUserPwd(Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""), getIntent().getStringExtra(OLD_PWD), mEtPwd.getText().toString());
                } else if (getIntent().getIntExtra(FROM_FORGET, 0) == 1) {
                    String sign = Constants.SALT_CIPHER + "confirmPassword=" + mEtPwd.getText().toString() + "&loginAccount=" + (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + getIntent().getStringExtra(COUNT) +
                            "&password=" + getIntent().getStringExtra(NEW_PWD) + "&pwdType=2&submitTime=" + Util.getNowTime() + "&verifyCode=" + getIntent().getStringExtra(CODE) + Constants.SALT_CIPHER;
                    sign = Util.encrypt(sign);
                    GZHApi.getInstance().restUserPwd(Util.getNowTime(), sign, getIntent().getStringExtra(CODE), mEtPwd.getText().toString(), getIntent().getStringExtra(NEW_PWD), (Util.isEmpty(UserPreference.getString(UserPreference.COUNTRY_SMS, "")) ? "+86" : UserPreference.getString(UserPreference.COUNTRY_SMS, "")) + getIntent().getStringExtra(COUNT));
                }
                break;
        }
    }

}
