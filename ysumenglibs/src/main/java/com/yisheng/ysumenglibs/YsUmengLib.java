package com.yisheng.ysumenglibs;

import android.content.Context;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

public class YsUmengLib {
    private static final String TAG = "YsUmengLib";

    public static void init(Context appContext, String appKey, String channelId, String UmengMessageSecret, final YsUmengInitCallBack callBack) {
        UMConfigure.init(appContext, appKey, channelId, UMConfigure.DEVICE_TYPE_PHONE, UmengMessageSecret);
        //获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(appContext);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.e(TAG, "注册成功：deviceToken：-------->  " + deviceToken);

                if (callBack != null) {
                    callBack.onGetDeviceToken(deviceToken);
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG, "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
                if (callBack != null) {
                    callBack.onGetDeviceTokenError(s, s1);
                }
            }
        });

    }

    public interface YsUmengInitCallBack {

        void onGetDeviceToken(String string);

        void onGetDeviceTokenError(String s, String s1);
    }

}