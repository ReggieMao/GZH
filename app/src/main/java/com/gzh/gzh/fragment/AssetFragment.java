package com.gzh.gzh.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.gzh.activity.AddAssetActivity;
import com.gzh.gzh.activity.Base1Activity;
import com.gzh.gzh.adapter.AssetAdapter;
import com.gzh.library.api.GZHApi;
import com.gzh.library.constant.Event;
import com.gzh.library.pojo.Income;
import com.gzh.library.pojo.RealmAsset;
import com.gzh.library.pojo.UpdateBalance;
import com.gzh.library.pull.AutoPullAbleGridView;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/7/23.
 * 资产页面
 */

public class AssetFragment extends Base1Fragment {

    private static final String TAG = "AssetFragment";
    private Base1Activity mBaseActivity;
    @Bind(R.id.gv_asset_list)
    AutoPullAbleGridView mListView;
    @Bind(R.id.tv_no_data)
    TextView mTvNoData;
    @Bind(R.id.pb)
    ProgressBar mPb;
    @Bind(R.id.pb1)
    ProgressBar mPb1;
    private AssetAdapter mAdapter;
    private List<Income> mAssets;
    private List<UpdateBalance> mUpdates;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_asset;
    }

    @Override
    public void initView() {
        mBaseActivity = (Base1Activity) getActivity();
        getAssetList();
    }

    @Override
    protected void initData() {
        super.initData();
        mBaseActivity.rxManage.on(Event.FIND_ALL_WALLET_LIST1, new Action1<List<Income>>() {
            @Override
            public void call(List<Income> o) {
                mAssets = new ArrayList<Income>();
                mUpdates = new ArrayList<UpdateBalance>();
                for (int i = 0; i < o.size(); i++) {
                    if (o.get(i).isAdd() && o.get(i).isShow())
                        mAssets.add(o.get(i));
                }
                UserPreference.putInt(UserPreference.ADD_COIN_COUNT, mAssets.size());
                for (int i = 0; i < mAssets.size(); i ++) {
                    String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + "&userWalletType=" + mAssets.get(i).getUserWalletType()
                            + UserPreference.getString(UserPreference.SECRET, "");
                    sign = Util.encrypt(sign);
                    GZHApi.getInstance().getIncome(3, UserPreference.getString(UserPreference.SESSION_ID, ""), sign, Util.getNowTime(), mAssets.get(i).getUserWalletType());
                }
                showAssetList(mAssets);
                setRealm(mAssets);
            }
        });

        mBaseActivity.rxManage.on(Event.GET_INCOME3, new Action1<Income>() {
            @Override
            public void call(Income o) {
                UpdateBalance updateBalance = new UpdateBalance();
                updateBalance.setBalance(o.getBalance());
                updateBalance.setUserWalletType(o.getUserWalletType());
                mUpdates.add(updateBalance);
                if ((mUpdates.size() == mAssets.size()) || (mUpdates.size() == (mAssets.size() - 1))) {
                    for (int i = 0; i < mAssets.size(); i ++) {
                        for (int d = 0; d < mUpdates.size(); d ++) {
                            if (mUpdates.get(d).getUserWalletType().equals(mAssets.get(i).getUserWalletType())) {
                                mAssets.get(i).setBalance(mUpdates.get(d).getBalance());
                            }
                        }
                    }
                    showAssetList(mAssets);
                    setRealm(mAssets);
                }
            }
        });

        mBaseActivity.rxManage.on(Event.UPDATE_ASSET, new Action1() {
            @Override
            public void call(Object o) {
                mPb1.setVisibility(View.VISIBLE);
                getAssetList();
            }
        });
    }

    private void setRealm(List<Income> assets) {
        Realm realm = Realm.getDefaultInstance();
        RealmAsset asset = new RealmAsset();
        RealmResults<RealmAsset> realmAssets = realm.where(RealmAsset.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmAssets.deleteAllFromRealm();
            }
        });
        for (int i = 0; i < assets.size(); i ++) {
            if (assets.get(i).isAdd() && assets.get(i).isShow()) {
                asset.setCoinName(assets.get(i).getCoinName());
                asset.setAddress(assets.get(i).getUserWalletAddress());
                asset.setBalance(assets.get(i).getBalance());
                asset.setAvalBalance(assets.get(i).getAvalBalance());
                asset.setCoinType(assets.get(i).getUserWalletType());
                asset.setCoinImgUrl(assets.get(i).getCoinImgUrl());
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(asset);
                    }
                });
            }
        }
    }

    private void getAssetList() {
        String sign = UserPreference.getString(UserPreference.SECRET, "") + "submitTime=" + Util.getNowTime() + UserPreference.getString(UserPreference.SECRET, "");
        sign = Util.encrypt(sign);
        GZHApi.getInstance().findAllWalletList(1, sign, Util.getNowTime(), UserPreference.getString(UserPreference.SESSION_ID, ""));
    }

    private void showAssetList(List<Income> list) {
        mPb.setVisibility(View.GONE);
        mPb1.setVisibility(View.GONE);
        if (null == mAdapter) {
            mAdapter = new AssetAdapter(getActivity(), list);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(list);
        }
        if (null != list && list.size() > 0) {
            mTvNoData.setVisibility(View.GONE);
        } else {
            mTvNoData.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.img_add})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_add:
                startActivity(new Intent(getActivity(), AddAssetActivity.class));
                break;
        }
    }

}
