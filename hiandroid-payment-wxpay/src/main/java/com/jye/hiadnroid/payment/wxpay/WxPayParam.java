package com.jye.hiadnroid.payment.wxpay;

import com.jye.hiandroid.payment.PayParam;

/**
 * @author jye
 */
public class WxPayParam extends PayParam {

    private String appId;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timeStamp;
    private String packageValue;
    private String sign;

    public String getAppId() {
        return appId;
    }

    public WxPayParam setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public WxPayParam setPartnerId(String partnerId) {
        this.partnerId = partnerId;
        return this;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public WxPayParam setPrepayId(String prepayId) {
        this.prepayId = prepayId;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public WxPayParam setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public WxPayParam setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public WxPayParam setPackageValue(String packageValue) {
        this.packageValue = packageValue;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public WxPayParam setSign(String sign) {
        this.sign = sign;
        return this;
    }
}

