package com.gzh.gzh.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.library.api.GZHApi;
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
 * 修改密码页面1
 */

public class ChangePwd1Activity extends Base1Activity {

    private static final String TAG = "ChangePwd1Activity";
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.tv_sure)
    TextView mTvSure;
    private boolean mOldRight = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_pwd1;
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
        rxManage.on(Event.CHECK_PAY_PWD, new Action1<String>() {
            @Override
            public void call(String o) {
                if (o.equals("支付密码错误")) {
                    ToastUtil.toast(ChangePwd1Activity.this, getString(R.string.pwd_error));
                } else if (o.equals("支付密码正确")) {
                    Intent intent = new Intent(ChangePwd1Activity.this, ChangePwd2Activity.class);
                    intent.putExtra(ChangePwd2Activity.OLD_PWD, mEtPwd.getText().toString());
                    intent.putExtra(ChangePwd2Activity.FROM_FORGET, 0);
                    startActivity(intent);
                }
            }
        });
    }

    private void nextBack() {
        if (mOldRight)
            mTvSure.setBackground(getResources().getDrawable(R.drawable.bg_round_text_green2));
        else
            mTvSure.setBackground(getResources().getDrawable(R.drawable.bg_round_text_gray1));
    }

    @OnClick({R.id.img_back, R.id.tv_sure, R.id.tv_forget})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_sure:
                if (mEtPwd.getText().length() != 6) {
                    ToastUtil.toast(this, getString(R.string.pwd_tip1));
                    return;
                }
                String sign = UserPreference.getString(UserPreference.SECRET, "") + "payPassword=" + Util.encrypt(mEtPwd.getText().toString()) + "&submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                sign = Util.encrypt(sign);
                GZHApi.getInstance().checkPayPassword(UserPreference.getString(UserPreference.SESSION_ID, ""), Util.getNowTime(), sign, Util.encrypt(mEtPwd.getText().toString()));
                break;
            case R.id.tv_forget:
                startActivity(new Intent(this, ResetPwdActivity.class));
                break;
        }
    }

}
