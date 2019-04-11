package com.gzh.gzh.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.gzh.gzh.R;
import com.gzh.library.constant.Constants;
import com.gzh.library.util.DialogUtil;
import com.gzh.library.util.ToastUtil;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;

/**
 * Created by MaoLJ on 2018/7/24.
 * 关于我们页面
 */

public class AboutUsActivity extends Base1Activity {

    private static final String TAG = "AboutUsActivity";
    @Bind(R.id.tv_new_version)
    TextView mTvVersion;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        mTvVersion.setText(getString(R.string.check_new1) + UserPreference.getString(UserPreference.VERSION_CODE, ""));
    }

    @OnClick({R.id.img_back, R.id.rl_agreement, R.id.rl_version, R.id.rl_privacy, R.id.rl_update})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_agreement:
                startActivity(new Intent(this, AgreementActivity.class));
                break;
            case R.id.rl_version:
                startActivity(new Intent(this, VersionLogActivity.class));
                break;
            case R.id.rl_privacy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            case R.id.rl_update:
                if (UserPreference.getString(UserPreference.VERSION_CODE, "").equals(Constants.APP_VERSION)) {
                    ToastUtil.toast(this, getString(R.string.check_new2));
                } else {
                    DialogUtil.versionUpdateDialog(this, UserPreference.getString(UserPreference.VERSION_CODE, ""), UserPreference.getString(UserPreference.VERSION_LOG, ""), new DialogUtil.OnResultListener0() {
                        @Override
                        public void onOK() {
                            Intent intent = new Intent(AboutUsActivity.this, AppDownloadActivity.class);
                            intent.putExtra(AppDownloadActivity.URL, UserPreference.getString(UserPreference.VERSION_URL, ""));
                            startActivity(intent);
                        }
                    });
                }
                break;
        }
    }

}
