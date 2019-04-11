package com.gzh.gzh.activity;

import android.view.View;

import com.gzh.gzh.R;
import com.gzh.library.util.Util;

import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/8/6.
 * 隐私条款页面
 */

public class PrivacyPolicyActivity extends Base1Activity {

    private static final String TAG = "PrivacyPolicyActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_privacy_policy;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
    }

    @OnClick({R.id.img_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

}
