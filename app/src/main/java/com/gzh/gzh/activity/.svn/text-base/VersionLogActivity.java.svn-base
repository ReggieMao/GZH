package com.gzh.gzh.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.library.util.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by MaoLJ on 2018/8/15.
 * 版本日志页面
 */

public class VersionLogActivity extends Base1Activity {

    private static final String TAG = "VersionLogActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_version_log;
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
