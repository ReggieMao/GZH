package com.gzh.gzh.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.library.util.ActivityUtil;
import com.gzh.library.util.ToastUtil;
import com.gzh.library.util.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/7/19.
 * 修改密码页面2
 */

public class ChangePwd2Activity extends Base1Activity {

    private static final String TAG = "ChangePwd2Activity";
    public static String FROM_FORGET = "from_forget";
    public static String OLD_PWD = "old_pwd";
    public static String CODE = "code";
    public static String COUNT = "count";
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.tv_sure)
    TextView mTvSure;
    private boolean mOldRight = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_pwd2;
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
                    ToastUtil.toast(this, getString(R.string.pwd_tip2));
                    return;
                }
                if (getIntent().getIntExtra(FROM_FORGET, 0) == 0) {
                    Intent intent = new Intent(this, ChangePwd3Activity.class);
                    intent.putExtra(ChangePwd3Activity.OLD_PWD, getIntent().getStringExtra(OLD_PWD));
                    intent.putExtra(ChangePwd3Activity.NEW_PWD, mEtPwd.getText().toString());
                    intent.putExtra(ChangePwd3Activity.FROM_FORGET, 0);
                    startActivity(intent);
                } else if (getIntent().getIntExtra(FROM_FORGET, 0) == 1) {
                    Intent intent = new Intent(this, ChangePwd3Activity.class);
                    intent.putExtra(ChangePwd3Activity.NEW_PWD, mEtPwd.getText().toString());
                    intent.putExtra(ChangePwd3Activity.FROM_FORGET, 1);
                    intent.putExtra(ChangePwd3Activity.CODE, getIntent().getStringExtra(CODE));
                    intent.putExtra(ChangePwd3Activity.COUNT, getIntent().getStringExtra(COUNT));
                    startActivity(intent);
                }
                break;
        }
    }

}
