package com.gzh.library.exception;

import android.net.ParseException;

import com.gzh.library.constant.GZHCode;
import com.gzh.library.constant.GZHStrings;
import com.google.gson.JsonParseException;
import org.json.JSONException;
import java.net.ConnectException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by MaoLJ on 2018/7/18.
 *
 */

public class ExceptionEngine {

    public static ApiException convertException(Throwable e) {
        ApiException ex = null;
        if (e instanceof HttpException) { //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, GZHCode.HTTP_ERROR);
            switch (httpException.code()) {
                case GZHCode.UNAUTHORIZED:
                case GZHCode.FORBIDDEN:
                case GZHCode.NOT_FOUND:
                case GZHCode.REQUEST_TIMEOUT:
                case GZHCode.GATEWAY_TIMEOUT:
                case GZHCode.INTERNAL_SERVER_ERROR:
                case GZHCode.BAD_GATEWAY:
                case GZHCode.SERVICE_UNAVAILABLE:
                default:
                    ex.setMessage(GZHStrings.NETWORK_ERROR);
                    break;
            }
            return ex;

        } else if (e instanceof ServerException) {//服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.getCode());
            ex.setMessage(resultException.getMessage());
            return ex;

        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {//均视为解析错误
            ex = new ApiException(e, GZHCode.PARSE_ERROR);
            ex.setMessage(GZHStrings.PARSE_ERROR);
            return ex;

        } else if (e instanceof ConnectException) {//均视为网络错误
            ex = new ApiException(e, GZHCode.NETWORD_ERROR);
            ex.setMessage(GZHStrings.NETWORK_ERROR);
            return ex;

        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, GZHCode.SSL_ERROR);
            ex.setMessage(GZHStrings.SSL_ERROR);
            return ex;

        } else {//未知错误
            ex = new ApiException(e, GZHCode.UNKNOWN);
            ex.setMessage(GZHStrings.UNKNOWN_ERROR);
            return ex;
        }
    }

}
