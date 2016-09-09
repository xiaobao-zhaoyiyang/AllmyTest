package com.retrofitdemo;

import android.util.Log;

/**
 * Created by yo on 2016/9/8.
 */
public class ApiException {
    public ApiException(int code){
//        Toast.makeText(ApiException.this, "数据获取错误", Toast.LENGTH_SHORT).show();
        Log.e("ApiException", "数据获取错误");
    }

}
