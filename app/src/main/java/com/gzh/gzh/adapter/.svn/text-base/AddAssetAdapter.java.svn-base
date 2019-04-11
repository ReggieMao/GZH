package com.gzh.gzh.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.library.api.GZHApi;
import com.gzh.library.pojo.Income;
import com.gzh.library.util.ImageLoadUtil;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;
import com.gzh.library.view.CommonVH;

import java.util.List;

/**
 * Created by MaoLJ on 2018/7/23.
 * 添加资产适配器
 */

public class AddAssetAdapter extends CommonListViewAdapter<Income> {

    private static final String TAG = "AddAssetAdapter";
    private Activity mActivity;
    public AddAssetAdapter(Activity activity, List<Income> datas) {
        super(activity, datas);
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonVH holder = CommonVH.get(mActivity, convertView, parent, R.layout.item_add_asset, position);

        LinearLayout layout = holder.getView(R.id.layout);
        TextView name = holder.getView(R.id.tv_name);
        ImageView logo = holder.getView(R.id.img_logo);
        ImageView select = holder.getView(R.id.img_select);

        Income coin = mDatas.get(position);

        name.setText(coin.getCoinName());
        if (coin.isAdd()) { // 已添加过
            if (coin.isShow())
                select.setImageResource(R.mipmap.choose);
            else {
                select.setImageResource(0);
                select.setBackgroundResource(R.drawable.bg_round_text_gray2);
            }
        } else // 未添加过
            select.setBackgroundResource(R.drawable.bg_round_text_gray2);
        ImageLoadUtil.loadServiceRoundImg(logo, coin.getCoinImgUrl(), R.mipmap.coin_default);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coin.isAdd()) {
                    if (coin.isShow()) {
                        select.setImageResource(0);
                        select.setBackgroundResource(R.drawable.bg_round_text_gray2);
                        coin.setShow(false);
                        String sign = UserPreference.getString(UserPreference.SECRET, "") + "coinType=" + coin.getUserWalletType() + "&isShow=false" + "&submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                        sign = Util.encrypt(sign);
                        GZHApi.getInstance().setWalletShow(Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""), false, coin.getUserWalletType());
                    } else {
                        select.setImageResource(R.mipmap.choose);
                        coin.setShow(true);
                        String sign = UserPreference.getString(UserPreference.SECRET, "") + "coinType=" + coin.getUserWalletType() + "&isShow=true" + "&submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
                        sign = Util.encrypt(sign);
                        GZHApi.getInstance().setWalletShow(Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""), true, coin.getUserWalletType());
                    }
                } else {
                    select.setImageResource(R.mipmap.choose);
                    coin.setAdd(true);
                    String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + "&userWalletType=" +
                            coin.getUserWalletType() + UserPreference.getString(UserPreference.SECRET, "");
                    sign = Util.encrypt(sign);
                    GZHApi.getInstance().addWallet(UserPreference.getString(UserPreference.SESSION_ID, ""), sign, Util.getNowTime(), coin.getUserWalletType());
                }
            }
        });

        return holder.getConvertView();
    }

}
