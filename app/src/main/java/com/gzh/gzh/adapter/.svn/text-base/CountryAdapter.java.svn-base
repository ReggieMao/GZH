package com.gzh.gzh.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.gzh.activity.NoticeInfoActivity;
import com.gzh.library.pojo.Country;
import com.gzh.library.pojo.Notice;
import com.gzh.library.view.CommonVH;
import com.yzq.zxinglibrary.common.Constant;

import java.util.List;

/**
 * Created by MaoLJ on 2018/7/23.
 * 国家短信适配器
 */

public class CountryAdapter extends CommonListViewAdapter<Country> {

    private static final String TAG = "CountryAdapter";
    private Activity mActivity;
    public CountryAdapter(Activity activity, List<Country> datas) {
        super(activity, datas);
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonVH holder = CommonVH.get(mActivity, convertView, parent, R.layout.item_country, position);

        LinearLayout layout = holder.getView(R.id.layout);
        TextView name = holder.getView(R.id.tv_country);
        TextView code = holder.getView(R.id.tv_code);

        Country country = mDatas.get(position);

        name.setText(country.getAddressName() + "/" + country.getAddressCode());
        code.setText("+" + country.getAddressPhone());
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mActivity.getIntent();
                intent.putExtra("code", country.getAddressPhone());
                mActivity.setResult(666, intent);
                mActivity.finish();
            }
        });

        return holder.getConvertView();
    }

}
