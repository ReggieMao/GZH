package com.gzh.library.exception;

import com.gzh.library.pojo.Income;
import com.gzh.library.pojo.RxResult;

import rx.functions.Func1;

/**
 * Created by MaoLJ on 2018/7/18.
 *
 */

public class ServerResultFunc1<T> implements Func1<RxResult<T>, T> {
    @Override
    public T call(RxResult<T> tRxResult) {
        if (tRxResult.getErrCode().equals("40004")) {
            Income income = new Income();
            income.setBalance(0);
            income.setUserWalletType("");
            return (T) income;
        } else {
            return tRxResult.getResult();
        }
    }
}

