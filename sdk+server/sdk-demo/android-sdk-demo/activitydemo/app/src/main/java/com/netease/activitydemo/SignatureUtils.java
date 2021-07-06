package com.netease.activitydemo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

public class SignatureUtils {

    /**
     * 生成签名信息
     *
     * @param secretKey 产品私钥
     * @param params    接口请求参数名和参数值map，不包括signature参数名
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String genSignature(String secretKey, Map<String, String> params)
            throws UnsupportedEncodingException {
        // 1. 参数名按照ASCII码表升序排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 2. 按照排序拼接参数名与参数值
        StringBuffer paramBuffer = new StringBuffer();
        for (String key : keys) {
            paramBuffer.append(key).append(params.get(key) == null ? "" : params.get(key));
        }
        // 3. 将secretKey拼接到最后
        paramBuffer.append(secretKey);

        // 4. MD5是128位长度的摘要算法，用16进制表示，一个十六进制的字符能表示4个位，所以签名后的字符串长度固定为32个十六进制字符。
        return md5(paramBuffer.toString().getBytes("UTF-8"));
    }

    public static String md5(byte[] bArr) {
        if (!(bArr == null || bArr.length == 0)) {
            try {
                byte[] digest = MessageDigest.getInstance("MD5").digest(bArr);
                StringBuilder sb = new StringBuilder(digest.length * 2);
                for (byte b : digest) {
                    int i = b & 255;
                    if (i < 16) {
                        sb.append("0");
                    }
                    sb.append(Integer.toHexString(i));
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException unused) {
            }
        }
        return "";
    }

}
