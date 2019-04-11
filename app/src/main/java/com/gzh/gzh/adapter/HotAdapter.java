package com.gzh.gzh.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gzh.library.pojo.Hot;

import java.util.List;

import com.gzh.gzh.R;
import com.gzh.library.util.ImageLoadUtil;
import com.gzh.library.view.CommonVH;

/**
 * Created by MaoLJ on 2018/7/23.
 * 热门适配器
 */

public class HotAdapter extends CommonListViewAdapter<Hot> {

    private static final String TAG = "HotAdapter";
    private Activity mActivity;
    private int mPosition;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public HotAdapter(Activity activity, List<Hot> datas) {
        super(activity, datas);
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonVH holder = CommonVH.get(mActivity, convertView, parent, R.layout.item_hot, position);

        ImageView img = holder.getView(R.id.img);

        Hot hot = mDatas.get(position);

        ImageLoadUtil.loadServiceImg(img, hot.getPageImgPath(), 0);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return holder.getConvertView();
    }

}
