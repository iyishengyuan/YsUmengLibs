package com.yisheng.ysumenglibs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

public class YsUmengLib {
    private static final String TAG = "YsUmengLib";
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";

    /**
     * 初始化 分析SDK
     *
     * @param context
     * @param umengAppKey
     * @param channelId   渠道号
     * @param umengSecret
     */
    public static void init(Context context, String umengAppKey, String channelId, String umengSecret) {
        UMConfigure.init(context, umengAppKey, channelId, UMConfigure.DEVICE_TYPE_PHONE, umengSecret);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    /**
     * 初始化 推送SDK
     *
     * @param context
     * @param packageName 主应用包名
     * @param isOpen      是否打开推送
     * @param callBack    成功失败的回调
     */
    public static void initPush(final Context context, String packageName, final boolean isOpen, final YsUmengInitCallBack callBack) {
        //获取消息推送代理示例
        final PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setResourcePackageName(packageName);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.e(TAG, "push 注册成功：deviceToken：-------->  " + deviceToken);
                context.sendBroadcast(new Intent(UPDATE_STATUS_ACTION));
                if (callBack != null) {
                    callBack.onGetDeviceToken(deviceToken);
                }

                if (isOpen) {
                    mPushAgent.enable(new IUmengCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(String s, String s1) {
                        }
                    });
                } else {
                    mPushAgent.disable(new IUmengCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(String s, String s1) {
                        }
                    });
                }

            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG, "push 注册失败：-------->  " + "s:" + s + ",s1:" + s1);
                if (callBack != null) {
                    callBack.onGetDeviceTokenError(s, s1);
                }
            }
        });
    }

    /**
     * 初始化 分享SDK
     *
     * @param context
     * @param WX_APP_ID
     * @param WX_APPSECRET
     */
    public static void initShare(final Context context, String WX_APP_ID, String WX_APPSECRET) {
        UMShareAPI.get(context);
        UMConfigure.setLogEnabled(false);
        PlatformConfig.setWeixin(WX_APP_ID, WX_APPSECRET);
        PlatformConfig.setWXFileProvider("com.tencent.sample2.fileprovider");
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, WX_APP_ID);
        boolean wxRegist = msgApi.registerApp(WX_APP_ID);
        Log.e(TAG, "分享微信注册 " + wxRegist + "  ,  isWXAppInstalled  " + msgApi.isWXAppInstalled());

    }

    public interface YsUmengInitCallBack {

        void onGetDeviceToken(String deviceToken);

        void onGetDeviceTokenError(String s, String s1);
    }

}