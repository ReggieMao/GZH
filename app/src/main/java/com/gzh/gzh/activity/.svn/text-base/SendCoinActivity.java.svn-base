package com.gzh.gzh.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzh.gzh.R;
import com.gzh.gzh.adapter.CoinAdapter;
import com.gzh.gzh.view.HorizontalListView;
import com.gzh.library.api.GZHApi;
import com.gzh.library.constant.ConstantCode;
import com.gzh.library.constant.Event;
import com.gzh.library.pojo.Asset;
import com.gzh.library.pojo.RealmAsset;
import com.gzh.library.pojo.RealmNotification;
import com.gzh.library.util.DialogUtil;
import com.gzh.library.util.ToastUtil;
import com.gzh.library.util.UserPreference;
import com.gzh.library.util.Util;
import com.jakewharton.rxbinding.view.RxView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MaoLJ on 2018/7/20.
 * 发币页面
 */

public class SendCoinActivity extends Base1Activity {

    private static final String TAG = "SendCoinActivity";
    private int REQUEST_CODE_SCAN = 1001;
    @Bind(R.id.ll_main)
    LinearLayout mLlMain;
    @Bind(R.id.list_view)
    HorizontalListView mListView;
    private CoinAdapter mAdapter;
    @Bind(R.id.et_address)
    EditText mEtAddress;
    @Bind(R.id.et_count)
    EditText mEtCount;
    @Bind(R.id.tv_type)
    TextView mTvType;
    @Bind(R.id.tv_can_pay)
    TextView mTvCanPay;
    @Bind(R.id.tv_miner)
    TextView mTvMiner;
    @Bind(R.id.img_go)
    ImageView mImgGo;
    @Bind(R.id.tv_send)
    TextView mTvSend;
    private List<Asset> mAssetList;
    private int mNowSelect = 2;
    private int mFeeType;
    private int mCurrentItem = 0;
    private int mLastItem = 0;
    private String mCount;
    private String mWalletType;
    private String mAddress;
    private Realm mRealm = Realm.getDefaultInstance();
    private String mCoinName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_coin;
    }

    @Override
    public void initView() {
        Util.immersiveStatus(this, true);
        Util.addLayoutListener(mLlMain, mTvSend);
        RxView.clicks(mTvSend).throttleFirst(2, TimeUnit.SECONDS);
        getCoinList();
        Util.setPoint(mEtCount);
    }

    @Override
    protected void initData() {
        super.initData();
        rxManage.on(Event.TRANSFER_TO_OTHERS1, new Action1() {
            @Override
            public void call(Object o) {
                ToastUtil.toast(SendCoinActivity.this, getString(R.string.toast_transform_success));
//                RealmNotification realmNotification = new RealmNotification();
//                realmNotification.setTimeStamp(System.currentTimeMillis());
//                realmNotification.setCoinType(mWalletType);
//                realmNotification.setHasRead(false);
//                realmNotification.setOut(true);
//                realmNotification.setTime(Util.getNowTime());
//                realmNotification.setBalance(Double.parseDouble(mEtCount.getText().toString()));
//                realmNotification.setCoinName(mCoinName);
//                realmNotification.setInAddress(mEtAddress.getText().toString());
//                mRealm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realm.copyToRealmOrUpdate(realmNotification);
//                    }
//                });
                finish();
            }
        });
    }

    private void getCoinList() {
        RealmResults<RealmAsset> realmAssets = mRealm.where(RealmAsset.class).findAll();
        mAssetList = new ArrayList<>();
        for (int i = 0; i < realmAssets.size(); i ++) {
            Asset asset = new Asset();
            asset.setCoinName(realmAssets.get(i).getCoinName());
            asset.setUserWalletAddress(realmAssets.get(i).getAddress());
            asset.setBalance(realmAssets.get(i).getBalance());
            asset.setAvalBalance(realmAssets.get(i).getAvalBalance());
            asset.setUserWalletType(realmAssets.get(i).getCoinType());
            asset.setCoinImgUrl(realmAssets.get(i).getCoinImgUrl());
            mAssetList.add(asset);
        }
        showCoinList(mAssetList);
        if (mAssetList.size() > 0)
            showCoinInfo(0);
        else
            ToastUtil.toast(this, getString(R.string.add_wallet));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLastItem = mCurrentItem;
                mCurrentItem = position;
                mAdapter.setPosition(mCurrentItem);
                mAdapter.notifyDataSetChanged();
                showCoinInfo(mCurrentItem);
            }
        });
    }

    private void showCoinInfo(int position) {
        if (mLastItem != mCurrentItem) {
            mEtAddress.setText("");
//            mEtCount.setText("");
            mNowSelect = 2;
        }
        mWalletType = mAssetList.get(position).getUserWalletType();
//        mCount = Util.point(mAssetList.get(position).getAvalBalance(), 6);
        mCount = Util.getScientificCountingMethodAfterData(mAssetList.get(position).getAvalBalance(), 6);
        mTvCanPay.setText(getString(R.string.group) + mCount + " " + mAssetList.get(position).getCoinName());
        mAddress = mAssetList.get(position).getUserWalletAddress();
        mCoinName = mAssetList.get(position).getCoinName();
        mImgGo.setVisibility(View.GONE);
        mTvType.setText("GZH/KB");
        mTvMiner.setText("0.004000");
    }

    private void showCoinList(List<Asset> list) {
        if (null != list && list.size() > 0) {
            if (null == mAdapter) {
                mAdapter = new CoinAdapter(this, list);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.setItems(list);
            }
        }
    }

    @OnClick({R.id.img_back, R.id.tv_send, R.id.et_address, R.id.img_sweep, R.id.ll_miner, R.id.tv_all})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_send:
                if (!mWalletType.equals("2") && Double.parseDouble(UserPreference.getString(UserPreference.GZH_AMOUNT, "")) < 1) {
                    ToastUtil.toast(this, getString(R.string.toast_gzh_too_small));
                    return;
                }
                if (mEtAddress.getText().toString().length() != 34) {
                    ToastUtil.toast(this, getString(R.string.toast_input_address));
                    return;
                }
                if (Util.isEmpty(mEtCount.getText().toString())) {
                    ToastUtil.toast(this, getString(R.string.toast_input_count));
                    return;
                }
                if (Double.parseDouble(mEtCount.getText().toString()) == 0) {
                    ToastUtil.toast(this, getString(R.string.toast_input_count0));
                    return;
                }
                if (Double.parseDouble(mEtCount.getText().toString()) > Double.parseDouble(mCount)) {
                    ToastUtil.toast(this, getString(R.string.toast_input_count1));
                    return;
                }
                if (mEtAddress.getText().toString().equals(mAddress)) {
                    ToastUtil.toast(this, getString(R.string.toast_input_count2));
                    return;
                }
                DialogUtil.transactionDialog(this, new DialogUtil.OnResultListener2() {
                    @Override
                    public void onOk(String... params) {
                        String sign = UserPreference.getString(UserPreference.SECRET, "") + "feeType=" + mFeeType + "&outAddress=" + mEtAddress.getText().toString() + "&payPassword=" +
                                Util.encrypt(params[0]) + "&submitTime=" + Util.getNowTime() + "&transferAmount=" + mEtCount.getText().toString() +
                                "&userWalletType=" + mWalletType + UserPreference.getString(UserPreference.SECRET, "");
                        sign = Util.encrypt(sign);
                        GZHApi.getInstance().transferToOthers(1, Util.getNowTime(), sign, UserPreference.getString(UserPreference.SESSION_ID, ""), mWalletType
                                , mEtAddress.getText().toString(), mEtCount.getText().toString(), Util.encrypt(params[0]), mFeeType);
                    }
                });
                break;
            case R.id.et_address:
                mEtAddress.setCursorVisible(true);
                break;
            case R.id.img_sweep:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 先判断是否有权限
                    if (AndPermission.hasPermission(SendCoinActivity.this, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // 有权限
                        Intent intent1 = new Intent(SendCoinActivity.this, CaptureActivity.class);
                        startActivityForResult(intent1, REQUEST_CODE_SCAN);
                    } else {
                        // 申请权限
                        AndPermission.with(SendCoinActivity.this).requestCode(ConstantCode.REQUEST_CODE_OF_CAMERA).callback(SendCoinActivity.this)
                                .permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).start();
                    }
                } else {
                    Intent intent1 = new Intent(SendCoinActivity.this, CaptureActivity.class);
                    startActivityForResult(intent1, REQUEST_CODE_SCAN);
                }
                break;
            case R.id.ll_miner:
//                if (!mCoinName.equals("SAS") && !mCoinName.equals("GZH")) {
//                    DialogUtil.minerFeeDialog(this, mNowSelect, new DialogUtil.OnResultListener1() {
//                        @Override
//                        public void select1() {
//                            mNowSelect = 1;
//                            mFeeType = 3;
//                            mTvType.setText(getString(R.string.first));
//                            switch (mCoinName) {
//                                case "ETH":
//                                    mTvMiner.setText("0.000300ETH");
//                                    break;
//                                case "BTC":
//                                    mTvMiner.setText("0.000300BTC");
//                                    break;
//                                case "GZH":
//                                    mTvMiner.setText("0.200000GZH");
//                                    break;
//                                default:
//                                    mTvMiner.setText("0.200000SAS");
//                                    break;
//                            }
//                        }
//
//                        @Override
//                        public void select2() {
//                            mNowSelect = 2;
//                            mFeeType = 2;
//                            mTvType.setText(getString(R.string.ordinary));
//                            switch (mCoinName) {
//                                case "ETH":
//                                    mTvMiner.setText("0.000200ETH");
//                                    break;
//                                case "BTC":
//                                    mTvMiner.setText("0.000200BTC");
//                                    break;
//                                case "GZH":
//                                    mTvMiner.setText("0.150000GZH");
//                                    break;
//                                default:
//                                    mTvMiner.setText("0.150000SAS");
//                                    break;
//                            }
//                        }
//
//                        @Override
//                        public void select3() {
//                            mNowSelect = 3;
//                            mFeeType = 1;
//                            mTvType.setText(getString(R.string.economics));
//                            switch (mCoinName) {
//                                case "ETH":
//                                    mTvMiner.setText("0.000100ETH");
//                                    break;
//                                case "BTC":
//                                    mTvMiner.setText("0.000100BTC");
//                                    break;
//                                case "GZH":
//                                    mTvMiner.setText("0.100000GZH");
//                                    break;
//                                default:
//                                    mTvMiner.setText("0.100000SAS");
//                                    break;
//                            }
//                        }
//                    });
//                }
                break;
            case R.id.tv_all:
                mEtCount.setText(mCount);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                String[] all = content.split(" ");
                if (all.length == 3) {
                    mEtAddress.setText(all[all.length - 1]);
                    mEtCount.setText("");
//                } else if (content.startsWith("gzh:")) {
                } else {
                    String qtAddress;
                    if (content.contains("?"))
                        qtAddress = content.substring(4, content.indexOf("?"));
                    else
                        qtAddress = content.substring(4);
                    String qtAmount;
                    if (content.contains("amount="))
                        qtAmount = content.substring(content.indexOf("=") + 1, content.contains("&") ? content.indexOf("&") : content.length());
                    else
                        qtAmount = "";
                    mEtAddress.setText(qtAddress);
                    mEtCount.setText(qtAmount);
                }
//                else
//                    ToastUtil.toast(SendCoinActivity.this, getString(R.string.code_not_right));
            }
        }
    }

    @PermissionYes(ConstantCode.REQUEST_CODE_OF_CAMERA)
    private void getCameraYes(List<String> grantedPermissions) {
        Intent intent = new Intent(SendCoinActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @PermissionNo(ConstantCode.REQUEST_CODE_OF_CAMERA)
    private void getCameraNo(List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            AndPermission.defaultSettingDialog(this, ConstantCode.REQUEST_CODE_OF_CAMERA).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

}
