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

    private CheckResult check(Map<String, String> params) {
        try {
            String response = HttpClient4Utils.sendPost(this.apiUrl, params);
            // 解析响应
            JsonObject jObject = new JsonParser().parse(response).getAsJsonObject();
            int code = jObject.get("code").getAsInt();
            String msg = jObject.get("msg").getAsString();
            if (code == 200) {
                // code==200说明接口正常
                JsonObject dataObject = jObject.getAsJsonObject("result");
                int action = dataObject.get("action").getAsInt();
                // hitType为命中原因，具体取值见文档
                int hitType = dataObject.get("hitType").getAsInt();
                if (action == 0) {
                    return CheckResult.NORMAL;
                } else if (action == 10) {
                    return CheckResult.SUSPECT;
                } else if (action == 20) {
                    return CheckResult.FATAL;
                }
            } else {
                // 接口调用出现错误
                // 请根据错误码 和 错误消息判断原因
                System.err.println(String.format("ERROR: code=%d, msg=%s", code, msg));
            }
        } catch (Exception e) {
            // log
            System.out.println("接口调用异常（超时 等），请检查网络是否正常");
        }

        return CheckResult.ERROR;
    }

    /**
     * 通过token查询结果
     * 
     * @param token 前端提交的查询token
     * @param businessParams 业务数据
     * @return
     * @throws Exception
     */
    public CheckResult check(String token, Map<String, String> businessParams) throws Exception {
        Map<String, String> params = new HashMap<String, String>();

        params.put("version", "200");
        params.put("secretId", getSecretId());
        params.put("businessId", getBusinessId());
        params.put("timestamp", System.currentTimeMillis() / 1000 + "");
        params.put("nonce", Math.random() + "");
        params.put("token", token);

        if (businessParams != null) {
            params.putAll(businessParams);
        }

        // 生成签名，参见签名过程的示例代码
        params.put("signature", SignatureUtils.genSignature(getSecretKey(), params));

        return check(params);
    }

}
