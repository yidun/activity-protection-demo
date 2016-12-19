package com.netease.is.ac.ap.demo.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.netease.is.ac.ap.sdk.ActivityProtectionChecker;
import com.netease.is.ac.ap.sdk.CheckResult;

public class RiseServlet extends HttpServlet {

    private static final long serialVersionUID = -7536482137254093489L;

    /** 产品密钥ID */
    private static final String SECRET_ID = "YOUR_SECRET_ID";

    /** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
    private static final String SECRET_KEY = "YOUR_SECRET_KEY";

    /** 业务ID，易盾根据产品业务特点分配 */
    private static final String BUSINESS_ID = "YOUR_BUSINESS_ID";

    private void doResponse(HttpServletResponse response, int code, String msg) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("code", code);
        json.addProperty("msg", msg);
        PrintWriter out = response.getWriter();
        out.println(json.toString());
        out.flush();
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        try {
            doResponse(response, 503, "处理错误");
        } catch (Exception e1) {
            // log
            e1.printStackTrace();
        }
    }

    private void handleCheckFatal(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 直接返回点赞失败
            doResponse(response, 400, "点赞失败");
        } catch (Exception e) {
            // log
            e.printStackTrace();
        }
    }

    private void handleCheckSuspect(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 建议做二次验证（比如：图片验证码、短信验证码等等）
            doResponse(response, 401, "点赞失败，建议增加二次验证（比如：图片验证码、短信验证码等）");
        } catch (Exception e) {
            // log
            e.printStackTrace();
        }
    }

    private void handleSuccess(HttpServletRequest request, HttpServletResponse response) {
        try {
            doResponse(response, 200, "点赞成功");
        } catch (Exception e) {
            // log
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=utf-8");
        // 线上环境请勿设置为*，否则会带来安全隐患
        // 这里是因为js sdk demo需要跨域访问才设置为*
        response.setHeader("Access-Control-Allow-Origin", "*");

        // 从请求中解析出token参数
        String token = request.getParameter("token");
        try {
            Map<String, String> businessParams = new HashMap<String, String>();
            /*
             * 以下是可选参数，如果有，建议提供，数据越多，识别效果越好
             * 
            params.put("account", "请替换成用户的唯一标识");
            params.put("email", "请替换成用户的邮箱");
            params.put("phone", "请替换成用户的手机号");
            params.put("ip", "请替换成用户点赞使用的IP");
            params.put("registerTime", "请替换成用户注册的时间（单位：秒）");
            params.put("registerIp", "请替换成用户注册时使用的ip");
            params.put("activityId", "请替换成活动的唯一标识");
            params.put("target", "请替换成活动的目标，比如：被点赞用户的唯一标识");
             */

            // 活动反作弊检测
            CheckResult result = new ActivityProtectionChecker(SECRET_ID, SECRET_KEY, BUSINESS_ID).check(token, businessParams);
            switch (result) {
                case NORMAL:
                    // 检测结果为：正常，不做特殊处理，继续后面的业务
                    break;
                case SUSPECT:
                    // 检测结果为：可疑，建议增加图形验证码or短信验证码等二次验证
                    handleCheckSuspect(request, response);
                    return;
                case FATAL:
                    // 检测结果为：作弊（明显的作弊），建议直接拒绝此次点赞
                    handleCheckFatal(request, response);
                    return;
            }
        } catch (Exception e) {
            handleException(request, response, e);
            return;
        }

        // 继续后面的业务逻辑

        // 这里仅做演示，直接返回
        handleSuccess(request, response);
    }
}
