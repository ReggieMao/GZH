package com.gzh.gzh.activity;

import android.view.View;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.gzh.adapter.CountryAdapter;
import com.gzh.library.api.GZHApi;
import com.gzh.library.constant.Event;
import com.gzh.library.pojo.Country;
import com.gzh.library.pull.AutoPullAbleGridView;
import com.gzh.library.util.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/7/24.
 * 国家列表页面
 */

public class CountryActivity extends Base1Activity {

    private static final String TAG = "CountryActivity";
    @Bind(R.id.gv_country_list)
    AutoPullAbleGridView mListView;
    @Bind(R.id.tv_no_data)
    TextView mTvNoData;
    private CountryAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_country;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        getSmsList();
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.GET_SMS_ADDRESS, new Action1<List<Country>>() {
            @Override
            public void call(List<Country> o) {
                showCountryList(o);
            }
        });
    }

    private void getSmsList() {
        GZHApi.getInstance().getSmsAddress();
    }

    private void showCountryList(List<Country> list) {
        if (null != list && list.size() > 0) {
            mTvNoData.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            if (null == mAdapter) {
                mAdapter = new CountryAdapter(this, list);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.setItems(list);
            }
        } else {
            mTvNoData.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            if (null != mAdapter) mAdapter.setItems(list);
        }
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
