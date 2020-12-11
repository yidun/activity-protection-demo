package com.netease.is.ac.ap.sdk;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netease.is.ac.ap.sdk.utils.HttpClient4Utils;
import com.netease.is.ac.ap.sdk.utils.SignatureUtils;

public abstract class AbstProtectionChecker {

    private String apiUrl;

    private String secretId;

    private String secretKey;

    private String businessId;

    protected String getApiUrl() {
        return apiUrl;
    }

    protected String getSecretId() {
        return secretId;
    }

    protected String getSecretKey() {
        return secretKey;
    }

    protected String getBusinessId() {
        return businessId;
    }

    protected AbstProtectionChecker(String apiUrl, String secretId, String secretKey, String businessId) {
        this.apiUrl = apiUrl;
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.businessId = businessId;
    }

    /**
     * 发送check请求，处理返回结果
     * 
     * @param params 请求参数
     * @return
     */
    private CheckResult check(Map<String, String> params) {
        // 发送check请求，业务方可以选择自己熟悉的工具包发送请求
        // 请特别注意，调用接口时，请设置http超时时间
        // 建议http超时的情况下，认为识别结果为“正常”
        String response = HttpClient4Utils.sendPost(this.apiUrl, params);
        try {
            // 解析响应
            JsonObject jObject = new JsonParser().parse(response).getAsJsonObject();
            int code = jObject.get("code").getAsInt();
            String msg = jObject.get("msg").getAsString();
            if (code == 200) {
                JsonObject dataObject = jObject.getAsJsonObject("result");
                System.out.println(dataObject);
                int action = dataObject.get("action").getAsInt();
                if (action == 0) {
                    System.out.println("正常（放行）");
                } else if (action == 10) {
                    System.out.println("正常（观察）");
                } else if (action == 20) {
                    System.out.println("致命（拦截）");
                }
                // 获取具体的设备信息
                // JsonObject deviceObj = dataObject.getAsJsonObject("detail");
            } else {
                System.out.println(String.format("ERROR: code=%d, msg=%s", code, msg));
            }
        } catch (Exception e) {
            System.out.println("接口调用异常（超时 等），当作[正常]处理");
            e.printStackTrace();
        }

        return CheckResult.ERROR;
    }

    /**
     * 封装公共参数和业务参数，发送check请求
     * 
     * @param token 前端提交的查询token
     * @param businessParams 业务数据
     * @return
     * @throws Exception
     */
    public CheckResult check(String token, Map<String, String> businessParams) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        // 这里填入公共参数
        params.put("version", "300");
        params.put("secretId", getSecretId());
        params.put("businessId", getBusinessId());
        params.put("timestamp", System.currentTimeMillis() / 1000 + "");
        params.put("nonce", Math.random() + "");
        params.put("token", token);

        if (businessParams != null) {
            params.putAll(businessParams);
        }

        // 生成签名，参见SignatureUtils#genSignature
        params.put("signature", SignatureUtils.genSignature(getSecretKey(), params));

        return check(params);
    }

}
