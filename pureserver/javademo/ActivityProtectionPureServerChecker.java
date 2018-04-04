package com.netease.is.ac.ap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netease.is.ac.ap.sdk.utils.HttpClient4Utils;
import com.netease.is.ac.ap.sdk.utils.SignatureUtils;
import org.apache.http.Consts;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class ActivityProtectionPureServerChecker {

    /** 产品密钥ID，产品标识 */
    private static final String SECRET_ID = "your secret id";

    /** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
    private static final String SECRET_KEY = "your secret key";

    /** 业务ID，易盾根据产品业务特点分配 */
    private static final String BUSINESS_ID = "your business id";

    /** 易盾营销反作弊检测接口地址 */
    private static final String API_URL = "https://ac.dun.163yun.com/pureserver/activity/check";

    /** 实例化HttpClient，发送http请求使用，可根据需要自行调参 */
    private static final HttpClient httpClient = HttpClient4Utils.createHttpClient(100, 20, 1000, 1000, 1000);

    public static void main(String[] args) throws Exception {

        String account = "100002";
        String email = "zhangsanzuiniu@163.com";
        String phone = "18888888888";
        String ip = "123.123.123.120";
        String registerTime = "1479178545";
        String registerIp = "123.123.123.123";
        String activityId = "168168";
        String target = "100003";

        Map<String, String> params = new HashMap<String, String>();
        params.put("version", "200");
        params.put("secretId", SECRET_ID);
        params.put("businessId", BUSINESS_ID);
        params.put("timestamp", System.currentTimeMillis() / 1000 + "");
        params.put("nonce", Math.random() + "");

        params.put("account", account);
        params.put("email", email);
        params.put("phone", phone);
        params.put("ip", ip);
        params.put("registerTime", registerTime);
        params.put("registerIp", registerIp);
        params.put("activityId", activityId);
        params.put("target", target);

        // 来自web端
        params.put("runEnv", "2");
        params.put("operatingTime", System.currentTimeMillis() / 1000 + "");

        // 接口文档中的其他参数，建议也一并提供

        // 生成签名，参见签名过程的示例代码
        params.put("signature", SignatureUtils.genSignature(SECRET_KEY, params));

        // 发送check请求，业务方可以选择自己熟悉的工具包发送请求
        // 请特别注意，调用接口时，请设置http超时时间
        // 建议http超时的情况下，认为识别结果为“正常”
        String response = HttpClient4Utils.sendPost(httpClient, API_URL, params, Consts.UTF_8);
        try {
            // 解析响应
            JsonObject jObject = new JsonParser()
                    .parse(response).getAsJsonObject();
            int code = jObject.get("code").getAsInt();
            String msg = jObject.get("msg").getAsString();
            if (code == 200) {
                JsonObject dataObject = jObject.getAsJsonObject("result");
                int action = dataObject.get("action").getAsInt();
                if (action == 0) {
                    System.out.println("正常");
                } else if (action == 10) {
                    System.out.println("嫌疑");
                } else if (action == 20) {
                    System.out.println("致命");
                }
            } else {
                System.out.println(String.format("ERROR: code=%d, msg=%s", code, msg));
            }
        } catch (Exception e) {
            System.out.println("接口调用异常（超时 等），当作[正常]处理");

            e.printStackTrace();
        }
    }
}