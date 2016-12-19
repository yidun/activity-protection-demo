package com.netease.is.ac.ap.sdk;

public class ActivityProtectionChecker extends AbstProtectionChecker {

    /**
     * 活动反作弊检测接口地址
     */
    private static final String API_URL = "https://ac.dun.163yun.com/v1/activity/check";

    /**
     * 
     * @param secretId 产品密钥ID
     * @param secretKey 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
     * @param businessId 业务ID，易盾根据产品业务特点分配
     */
    public ActivityProtectionChecker(String secretId, String secretKey, String businessId) {
        super(API_URL, secretId, secretKey, businessId);
    }
}
