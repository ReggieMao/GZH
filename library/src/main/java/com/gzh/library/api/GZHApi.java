package com.gzh.library.api;

import android.util.Log;

import com.gzh.library.base.RxManage;
import com.gzh.library.constant.Event;
import com.gzh.library.exception.ApiExceptionFunc;
import com.gzh.library.exception.ServerResultFunc;
import com.gzh.library.exception.ServerResultFunc1;
import com.gzh.library.pojo.AppVersion;
import com.gzh.library.pojo.Asset;
import com.gzh.library.pojo.Balance;
import com.gzh.library.pojo.Country;
import com.gzh.library.pojo.Income;
import com.gzh.library.pojo.Login;
import com.gzh.library.pojo.Market;
import com.gzh.library.pojo.Register;
import com.gzh.library.pojo.Trade;
import com.gzh.library.pojo.UserInfo;
import com.gzh.library.pojo.Wallet;
import com.gzh.library.rxjava.GZHSubscriber;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.schedulers.Schedulers;

/**
 * Created by MaoLj on 2018/7/26.
 * 具体api
 */

public class GZHApi {

    private static final String TAG = "GZHApi";
    private GZHApi() {}
    private static GZHApi gzhApi = new GZHApi();
    public static GZHApi getInstance() {
        return gzhApi;
    }

    // 发送验证码
    public void getAuthCode(int type, String mobile, String sign, String submitTime) {
        Api.getInstance().service.getAuthCode(mobile, sign, submitTime)
                .map(new ServerResultFunc<String>())
                .onErrorResumeNext(new ApiExceptionFunc<String>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<String>() {
                    @Override
                    public void onNext(String data) {
                        switch (type) {
                            case 0:
                                new RxManage().post(Event.GET_AUTH_CODE, data);
                                break;
                            case 1:
                                new RxManage().post(Event.GET_AUTH_CODE1, data);
                                break;
                            case 2:
                                new RxManage().post(Event.GET_AUTH_CODE2, data);
                                break;
                        }
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 校验验证码
    public void checkAuthCode(int type, String verifyCode, String mobile, String sign, String submitTime) {
        Api.getInstance().service.checkAuthCode(sign, submitTime, mobile, verifyCode)
                .map(new ServerResultFunc<String>())
                .onErrorResumeNext(new ApiExceptionFunc<String>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<String>() {
                    @Override
                    public void onNext(String data) {
                        if (type == 0)
                            new RxManage().post(Event.CHECK_AUTH_CODE, data);
                        else
                            new RxManage().post(Event.CHECK_AUTH_CODE1, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 注册
    public void register(String bindMobile, String passWord, String payPassWord, String sign, String submitTime, String nickName, String verifyCode) {
        Api.getInstance().service.register(bindMobile, passWord, payPassWord, sign, submitTime, nickName, verifyCode)
                .map(new ServerResultFunc<Register>())
                .onErrorResumeNext(new ApiExceptionFunc<Register>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Register>() {
                    @Override
                    public void onNext(Register data) {
                        new RxManage().post(Event.REGISTER, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 登录
    public void userLogin(String loginAccount, String verifyCode, String sign, String submitTime) {
        Api.getInstance().service.userLogin(loginAccount, verifyCode, sign, submitTime)
                .map(new ServerResultFunc<Login>())
                .onErrorResumeNext(new ApiExceptionFunc<Login>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Login>() {
                    @Override
                    public void onNext(Login data) {
                        new RxManage().post(Event.LOGIN, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 个人钱包列表+个人总资产
    public void findList(String appSessionId, String sign, String submitTime) {
        Api.getInstance().service.findList(appSessionId, sign, submitTime)
                .map(new ServerResultFunc<Wallet>())
                .onErrorResumeNext(new ApiExceptionFunc<Wallet>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Wallet>() {
                    @Override
                    public void onNext(Wallet data) {
                        new RxManage().post(Event.FIND_LIST, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 可添加钱包列表
    public void findAllWalletList(int where, String sign, String submitTime, String appSessionId) {
        Api.getInstance().service.findAllWalletList(sign, submitTime, appSessionId)
                .map(new ServerResultFunc<List<Income>>())
                .onErrorResumeNext(new ApiExceptionFunc<List<Income>>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<List<Income>>() {
                    @Override
                    public void onNext(List<Income> data) {
                        if (where == 0)
                            new RxManage().post(Event.FIND_ALL_WALLET_LIST, data);
                        else if (where == 1)
                            new RxManage().post(Event.FIND_ALL_WALLET_LIST1, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 添加钱包
    public void addWallet(String appSessionId, String sign, String submitTime, String userWalletType) {
        Api.getInstance().service.addWallet(sign, submitTime, appSessionId, userWalletType)
                .map(new ServerResultFunc<Asset>())
                .onErrorResumeNext(new ApiExceptionFunc<Asset>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Asset>() {
                    @Override
                    public void onNext(Asset data) {
                        new RxManage().post(Event.ADD_WALLET, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 查询用户对应钱包金额
    public void getIncome(int type, String appSessionId, String sign, String submitTime, String userWalletType) {
        if (type != 3) {
            Api.getInstance().service.getIncome(sign, submitTime, appSessionId, userWalletType)
                    .map(new ServerResultFunc<Income>())
                    .onErrorResumeNext(new ApiExceptionFunc<Income>())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new GZHSubscriber<Income>() {
                        @Override
                        public void onNext(Income data) {
                            switch (type) {
                                case 0:
                                    new RxManage().post(Event.GET_INCOME, data);
                                    break;
                                case 1:
                                    new RxManage().post(Event.GET_INCOME1, data);
                                    break;
                                case 2:
                                    new RxManage().post(Event.GET_INCOME2, data);
                                    break;
                                case 4:
                                    new RxManage().post(Event.GET_INCOME4, data);
                                    break;
                            }
                            Log.d(TAG, "result--->success: " + data);
                        }
                    });
        } else {
            Api.getInstance().service.getIncome(sign, submitTime, appSessionId, userWalletType)
                    .map(new ServerResultFunc1<Income>())
                    .onErrorResumeNext(new ApiExceptionFunc<Income>())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new GZHSubscriber<Income>() {
                        @Override
                        public void onNext(Income data) {
                            new RxManage().post(Event.GET_INCOME3, data);
                            Log.d(TAG, "result--->success: " + data);
                        }
                    });
        }
    }

    // 查询交易明细
    public void findBalanceList(int where, String appSessionId, String sign, String submitTime, String userWalletType, int count) {
        Api.getInstance().service.findBalanceList(sign, submitTime, appSessionId, userWalletType, count)
                .map(new ServerResultFunc<List<Balance>>())
                .onErrorResumeNext(new ApiExceptionFunc<List<Balance>>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<List<Balance>>() {
                    @Override
                    public void onNext(List<Balance> data) {
                        if (where == 0)
                            new RxManage().post(Event.FIND_BALANCE_LIST, data);
                        else if (where == 1)
                            new RxManage().post(Event.FIND_BALANCE_LIST1, data);
                        else if (where == 2)
                            new RxManage().post(Event.FIND_BALANCE_LIST2, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 转币
    public void transferToOthers(int type, String submitTime, String sign, String appSessionId, String userWalletType, String outAddress, String transferAmount, String payPassword, int feeType) {
        Api.getInstance().service.transferToOthers(submitTime, sign, appSessionId, userWalletType, outAddress, transferAmount, payPassword, feeType)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        if (type == 0)
                            new RxManage().post(Event.TRANSFER_TO_OTHERS, data);
                        else if (type == 1)
                            new RxManage().post(Event.TRANSFER_TO_OTHERS1, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 个人信息
    public void getUserInfo(String submitTime, String sign, String appSessionId) {
        Api.getInstance().service.getUserInfo(submitTime, sign, appSessionId)
                .map(new ServerResultFunc<UserInfo>())
                .onErrorResumeNext(new ApiExceptionFunc<UserInfo>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<UserInfo>() {
                    @Override
                    public void onNext(UserInfo data) {
                        new RxManage().post(Event.GET_USER_INFO, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 行情
    public void getTradeInfo(String submitTime, String sign, String appSessionId) {
        Api.getInstance().service.getTradeInfo(submitTime, sign, appSessionId)
                .map(new ServerResultFunc<List<Trade>>())
                .onErrorResumeNext(new ApiExceptionFunc<List<Trade>>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<List<Trade>>() {
                    @Override
                    public void onNext(List<Trade> data) {
                        new RxManage().post(Event.GET_TRADE_INFO, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 昵称头像修改
    public void resetNickNameAndImg(int what, String submitTime, String sign, String appSessionId, String nickName, String imageUrl) {
        Api.getInstance().service.resetNickNameAndImg(submitTime, sign, appSessionId, nickName, imageUrl)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        if (what == 0)
                            new RxManage().post(Event.UPDATE_NAME, data);
                        else if (what == 1)
                            new RxManage().post(Event.UPDATE_AVATAR, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 资讯
    public void findNewList(int where, String submitTime, String sign, String appSessionId) {
        Api.getInstance().service.findNewList(submitTime, sign, appSessionId)
                .map(new ServerResultFunc<Market>())
                .onErrorResumeNext(new ApiExceptionFunc<Market>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Market>() {
                    @Override
                    public void onNext(Market data) {
                        if (where == 0)
                            new RxManage().post(Event.FIND_NEWS_LIST, data);
                        else if (where == 1)
                            new RxManage().post(Event.FIND_NEWS_LIST1, data);
                        else if (where == 2)
                            new RxManage().post(Event.FIND_NEWS_LIST2, data);
                        else
                            new RxManage().post(Event.FIND_NEWS_LIST3, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 上传registerId
    public void registerId(String submitTime, String sign, String appSessionId, String audienceId) {
        Api.getInstance().service.registerId(submitTime, sign, appSessionId, audienceId)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.REGISTER_ID, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 修改支付密码
    public void updateUserPwd(String submitTime, String sign, String appSessionId, String oldPassword, String password) {
        Api.getInstance().service.updateUserPwd(submitTime, sign, appSessionId, oldPassword, password, 2)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.UPDATE_PWD, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 忘记密码
    public void restUserPwd(String submitTime, String sign, String verifyCode, String confirmPassword, String password, String loginAccount) {
        Api.getInstance().service.restUserPwd(submitTime, sign, verifyCode, confirmPassword, password, 2, loginAccount)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.RESET_PWD, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 设置是否显示钱包
    public void setWalletShow(String submitTime, String sign, String appSessionId, boolean isShow, String coinType) {
        Api.getInstance().service.setWalletShow(submitTime, sign, appSessionId, isShow, coinType)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.SET_WALLET_SHOW, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 全部已读
    public void markAllRead(String submitTime, String sign, String appSessionId) {
        Api.getInstance().service.markAllRead(submitTime, sign, appSessionId)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.MARK_ALL_READ, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 汇率
    public void usdToCny(String submitTime, String sign, String appSessionId) {
        Api.getInstance().service.usdToCny(submitTime, sign, appSessionId)
                .map(new ServerResultFunc<Double>())
                .onErrorResumeNext(new ApiExceptionFunc<Double>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Double>() {
                    @Override
                    public void onNext(Double data) {
                        new RxManage().post(Event.USD_TO_CNY, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 上传用户头像
    public void uploadAvatar(File file, String appSessionId) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Api.getInstance().service.uploadAvatar(body, appSessionId)
                .map(new ServerResultFunc<String>())
                .onErrorResumeNext(new ApiExceptionFunc<String>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<String>() {
                    @Override
                    public void onNext(String data) {
                        new RxManage().post(Event.UPLOAD_AVATAR, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 退出登录
    public void logout(String submitTime, String sign, String appSessionId) {
        Api.getInstance().service.logout(submitTime, sign, appSessionId)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.LOGOUT, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 判断手机号是否已注册
    public void isRegister(String submitTime, String sign, String loginAccount) {
        Api.getInstance().service.isRegister(submitTime, sign, loginAccount)
                .map(new ServerResultFunc<Object>())
                .onErrorResumeNext(new ApiExceptionFunc<Object>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<Object>() {
                    @Override
                    public void onNext(Object data) {
                        new RxManage().post(Event.IS_REGISTER, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 校验支付密码
    public void checkPayPassword(String appSessionId, String submitTime, String sign, String payPassword) {
        Api.getInstance().service.checkPayPassword(appSessionId, submitTime, sign, payPassword)
                .map(new ServerResultFunc<String>())
                .onErrorResumeNext(new ApiExceptionFunc<String>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<String>() {
                    @Override
                    public void onNext(String data) {
                        new RxManage().post(Event.CHECK_PAY_PWD, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 获取最新版本
    public void getLatestAppVersion(String appSystem) {
        Api.getInstance().service.getLatestAppVersion(appSystem)
                .map(new ServerResultFunc<AppVersion>())
                .onErrorResumeNext(new ApiExceptionFunc<AppVersion>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<AppVersion>() {
                    @Override
                    public void onNext(AppVersion data) {
                        new RxManage().post(Event.APP_VERSION, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

    // 获取各国手机号代码
    public void getSmsAddress() {
        Api.getInstance().service.getSmsAddress()
                .map(new ServerResultFunc<List<Country>>())
                .onErrorResumeNext(new ApiExceptionFunc<List<Country>>())
                .subscribeOn(Schedulers.io())
                .subscribe(new GZHSubscriber<List<Country>>() {
                    @Override
                    public void onNext(List<Country> data) {
                        new RxManage().post(Event.GET_SMS_ADDRESS, data);
                        Log.d(TAG, "result--->success: " + data);
                    }
                });
    }

}
