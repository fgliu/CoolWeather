package com.fgliu.coolweather.util;

/**
 * Created by andy on 16/6/26.
 */
public interface HttpCallBackListener {

    void onFinish(String response);

    void onError(Exception e);
}
