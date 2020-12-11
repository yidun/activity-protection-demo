//易盾反作弊CSharp示例代码
//接口文档: https://support.dun.163.com/documents/15588071870066688?docId=429073890435571712
//开发工具 visual studio 2019 社区版，.Net 版本 4.5.2+
//author = 'yidun-dev'
//date = '2020/12/10'
//version = '0.1'

﻿using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;

namespace ActivityProtectionDemo.Sdk
{
    public enum CheckResultType
    {
        Success = 0,
        Suspicion = 10,
        ConfirmedCheat = 20,
        BadRequest = 400,
        Forbidden = 401,
        ParamError = 405,
        SignatureFailure = 410,
        RequestExpired = 420,
        ReplayAttack = 430,
        DecodeError = 440,
        WrongToken = 450,
        ServiceUnavailable = 503,
        Error = -1,
    };

    /// <summary>
    /// 活动反作弊验证
    /// </summary>
    class ActivityAntiCheatChecker
    {
        /** 产品密钥ID */
        private static string SECRET_ID = "YOUR_SECRET_ID";
        /** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
        private static string SECRET_KEY = "YOUR_SECRET_KEY";
        /** 业务ID，易盾根据产品业务特点分配 */
        private static string BUSINESS_ID = "YOUR_BUSINESS_ID";

        private static string VERSION = "300";
        private static string CHECK_URL = "https://ac.dun.163.com/v3/common/check"; // check接口地址
        private static HttpClient client = Utils.makeHttpClient();


        /// <summary>
        /// 简单测试
        /// </summary>
        /// <returns></returns>
        public static void Main(string[] args)
        {
            //前端提交的查询token
            string token = "YOUR_FRONT_TOKEN";

            Dictionary<String, String> parameters = new Dictionary<String, String>();
            long curr = (long)(DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalMilliseconds/1000;
            String time = curr.ToString();


            // 1.设置公共参数
            parameters.Add("businessId", BUSINESS_ID);
            parameters.Add("secretId", SECRET_ID);
            parameters.Add("version", VERSION);
            parameters.Add("timestamp", time);
            parameters.Add("nonce", new Random().Next().ToString());
            parameters.Add("token", token);

            //业务可选参数 尽量详细添加
            //parameters.Add("account", "请替换成用户的唯一标识");
            //parameters.Add("email", "请替换成用户的邮箱");
            //parameters.Add("phone", "请替换成用户的手机号");
            //parameters.Add("ip", "请替换成用户点赞使用的IP");
            //parameters.Add("registerTime", "请替换成用户注册的时间（单位：秒）");
            //parameters.Add("registerIp", "请替换成用户注册时使用的ip");
            //parameters.Add("activityId", "请替换成活动的唯一标识");
            //parameters.Add("target", "请替换成活动的目标，比如：被点赞用户的唯一标识");
            //parameters.Add("nickname", "请替换成用户昵称，比如：昵称");
            //parameters.Add("userLevel", "请替换成用户等级，比如：VIP用户");
            //parameters.Add("extData", "附加数据，json格式");

            // 2.生成签名信息
            String signature = Utils.genSignature(SECRET_KEY, parameters);
            parameters.Add("signature", signature);

            // 3.发送HTTP请求
            String response = Utils.doPost(client, CHECK_URL, parameters, 5000);
            Console.WriteLine(parseRet(response));
        }

        /// <summary>
        /// 解析cehck接口返回的结果
        /// </summary>
        /// <param name="response"></param>
        /// <returns></returns>
        static CheckResultType parseRet(string response)
        {
            CheckResultType returnResult = CheckResultType.Success;
            if (String.IsNullOrEmpty(response))
            {
                returnResult = CheckResultType.Error;
                return returnResult;
            }
            try
            {
                JObject ret = JObject.Parse(response);
                int code = ret.GetValue("code").ToObject<Int32>();
                String msg = ret.GetValue("msg").ToObject<String>();
                if (code == 200)
                {
                    JObject array = (JObject)ret.SelectToken("result");
                    int actioncode = array.GetValue("action").ToObject<Int32>();

                    returnResult = (CheckResultType)actioncode;
                }
                else
                {
                    Console.WriteLine("error: {0}", msg);
                    returnResult = (CheckResultType)code;
                }
            }
            catch (Exception e)
            {
                if (e.Source != null)
                {
                    Console.WriteLine("IOException source: {0}", e.Source);
                }
                returnResult = CheckResultType.Error;

            }
            return returnResult;
        }
    }

}