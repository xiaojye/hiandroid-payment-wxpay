package com.jye.hiadnroid.payment.wxpay;

import android.content.Context;
import android.text.TextUtils;

import com.jye.hiandroid.payment.PayCallback;
import com.jye.hiandroid.payment.PayErrCode;
import com.jye.hiandroid.payment.PayStrategy;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author jye
 */
public class WxPay extends PayStrategy<WxPayParam> {

    private Context mContext;
    private IWXAPI mWXApi;
    private boolean mInitialized;
    private PayCallback mPayCallback;
    private static WxPay sInstance = null;

    static WxPay getInstance() {
        return sInstance;
    }

    IWXAPI getWXApi() {
        return mWXApi;
    }

    private void initWXApi(String appId) {
        this.mWXApi = WXAPIFactory.createWXAPI(mContext.getApplicationContext(), appId);
        this.mWXApi.registerApp(appId);
        this.mInitialized = true;
    }

    @Override
    protected void setContext(Context context) {
        this.mContext = context;
        sInstance = this;
    }

    /**
     * 检查是否安装了微信APP且支持微信支付
     */
    private boolean checkInstalledWechat() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    /**
     * 检查参数
     *
     * @param param
     * @param callback
     * @return
     */
    private boolean checkParam(WxPayParam param, PayCallback callback) {
        if (param == null) {
            if (callback != null) {
                callback.onFailure(PayErrCode.PARAMETER_INVALID, "支付参数为空");
            }
            return false;
        }
        if (TextUtils.isEmpty(param.getAppId())) {
            if (callback != null) {
                callback.onFailure(PayErrCode.PARAMETER_INVALID, "缺少'appId'参数");
            }
            return false;
        }
        if (TextUtils.isEmpty(param.getAppId())) {
            if (callback != null) {
                callback.onFailure(PayErrCode.PARAMETER_INVALID, "缺少'appId'参数");
            }
            return false;
        }

        return true;
    }

    @Override
    public void doPay(WxPayParam param, PayCallback callback) {
        this.mPayCallback = callback;

        if (!checkParam(param, callback)) {
            return;
        }

        if (!mInitialized) {
            initWXApi(param.getAppId());
        }

        if (!checkInstalledWechat()) {
            if (callback != null) {
                callback.onFailure(PayErrCode.UNSUPPORT, "未安装微信或者微信版本太低");
                return;
            }
        }

        PayReq req = new PayReq();
        req.appId = param.getAppId();
        req.partnerId = param.getPartnerId();
        req.prepayId = param.getPrepayId();
        req.packageValue = param.getPackageValue();
        req.nonceStr = param.getNonceStr();
        req.timeStamp = param.getTimeStamp();
        req.sign = param.getSign();

        mWXApi.sendReq(req);
    }

    /**
     * 支付回调响应
     *
     * @param errCode 错误码
     * @param errDesc 错误描述
     */
    void onResp(int errCode, String errDesc) {
        if (mPayCallback == null) {
            return;
        }

        if (errCode == BaseResp.ErrCode.ERR_OK) { //成功
            mPayCallback.onSuccess();
        } else if (errCode == BaseResp.ErrCode.ERR_USER_CANCEL) { //取消
            mPayCallback.onCancel();
        } else { //失败
            if (TextUtils.isEmpty(errDesc)) {
                errDesc = "支付失败";
            }
            mPayCallback.onFailure(errCode, errDesc);
        }

        mPayCallback = null;
    }
}
