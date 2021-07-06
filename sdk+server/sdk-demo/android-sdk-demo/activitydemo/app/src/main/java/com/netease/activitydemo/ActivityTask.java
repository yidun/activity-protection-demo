package com.netease.activitydemo;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.netease.mobsec.GetTokenCallback;
import com.netease.mobsec.WatchMan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ActivityTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private String mSecretId;
    private String mBusinessId;
    private String mSecretKey;
    public static String TAG = "Activity-getToken";

    public ActivityTask(Context context, String secretId, String businessId, String secretKey) {
        this.mContext = context;
        this.mSecretId = secretId;
        this.mBusinessId = businessId;
        this.mSecretKey = secretKey;
    }

    private String mToken = "";

    @Override
    protected String doInBackground(String... strings) {
        final CountDownLatch latch = new CountDownLatch(1);
        WatchMan.getToken(new GetTokenCallback() {
            @Override
            public void onResult(int code, String msg, String Token) {
                Log.e(TAG, "Register, code = " + code + " msg = " + msg + " Token:" + Token);
                mToken = Token;
                latch.countDown();
            }
        });
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", mToken);
        params.put("version", "300");
        params.put("secretId", mSecretId);
        params.put("businessId", mBusinessId);
        params.put("timestamp", System.currentTimeMillis() / 1000 + "");
        params.put("nonce", Math.random() + "");
        try {
            params.put("signature", SignatureUtils.genSignature(mSecretKey, params));
        } catch (UnsupportedEncodingException e) {

        }
        String result = PostData(params);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (!TextUtils.isEmpty(result))
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
    }

    public String getRequestData(Map<String, String> params, String encode) {
        StringBuilder stringBuffer = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }


    private String showResponseResult(InputStream inptStream) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inptStream));
            String line = "";
            while (null != (line = reader.readLine())) {
                result.append(line);
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public String PostData(Map<String, String> params) {
        byte[] data = getRequestData(params, "utf-8").getBytes();
        String url = "https://ac.dun.163.com/v3/common/check";
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return showResponseResult(inptStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
