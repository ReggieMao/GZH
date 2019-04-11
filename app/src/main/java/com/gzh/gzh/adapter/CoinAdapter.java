package com.gzh.gzh.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.library.pojo.Asset;
import com.gzh.library.util.ImageLoadUtil;
import com.gzh.library.view.CommonVH;

import java.util.List;

/**
 * Created by MaoLJ on 2018/7/23.
 * 币种适配器
 */

public class CoinAdapter extends CommonListViewAdapter<Asset> {

    private static final String TAG = "CoinAdapter";
    private Activity mActivity;
    private int mPosition;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public CoinAdapter(Activity activity, List<Asset> datas) {
        super(activity, datas);
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonVH holder = CommonVH.get(mActivity, convertView, parent, R.layout.item_coin, position);

        RelativeLayout layout = holder.getView(R.id.layout);
        ImageView logo = holder.getView(R.id.img_logo);
        TextView name = holder.getView(R.id.tv_name);

        Asset coin = mDatas.get(position);

        if (position == getPosition())
            layout.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_round_edit_white));
        else
            layout.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_round_edit_gray5));
        ImageLoadUtil.loadServiceRoundImg(logo, coin.getCoinImgUrl(), R.mipmap.coin_default);
        name.setText(coin.getCoinName());

        return holder.getConvertView();
    }

}
