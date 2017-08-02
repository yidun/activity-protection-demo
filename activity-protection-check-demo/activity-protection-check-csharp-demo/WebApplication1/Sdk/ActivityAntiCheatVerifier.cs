using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web;

namespace ActivityProtectionDemo.Sdk
{

    public enum VerifyResultType
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
    /// 易盾验证码二次校验接口简单封装demo
    /// </summary>
    public class ActivityAntiCheatVerifier
    {
        public static string VERIFY_API = "https://ac.dun.163yun.com/v1/activity/check"; // verify接口地址

        private NESecretPair secretPair; // 密钥对
        private readonly string VERSION = "v1.1";
        private readonly HttpClient client = Utils.makeHttpClient();
        private string BUSINESS_ID;

        public ActivityAntiCheatVerifier( NESecretPair secretPair, string businessID)
        {
            this.secretPair = secretPair;
            BUSINESS_ID = businessID;
        }
        
        /// <summary>
        /// 向易盾验证码后台发起二次校验请求
        /// </summary>
        /// <param name="validate">二次校验请求字符串</param>
        /// <param name="user">当前用户信息，可以为空字符串</param>
        /// <returns></returns>
        public VerifyResultType verify(string token)
        {
            //can add your own user validation logic 


            Dictionary<String, String> parameters = new Dictionary<String, String>();
            long curr = (long)(DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc)).TotalMilliseconds/1000;
            String time = curr.ToString();


            // 1.设置公共参数
            parameters.Add("businessId", BUSINESS_ID);
            parameters.Add("secretId", secretPair.secretId);
            parameters.Add("version", VERSION);
            parameters.Add("timestamp", time);
            parameters.Add("nonce", new Random().Next().ToString());
            parameters.Add("token", token);

            //可选参数 尽量详细添加
            //parameters.put("account", "请替换成用户的唯一标识");
            //parameters.put("email", "请替换成用户的邮箱");
            //parameters.put("phone", "请替换成用户的手机号");
            //parameters.put("ip", "请替换成用户点赞使用的IP");
            //parameters.put("registerTime", "请替换成用户注册的时间（单位：秒）");
            //parameters.put("registerIp", "请替换成用户注册时使用的ip");
            //parameters.put("activityId", "请替换成活动的唯一标识");
            //parameters.put("target", "请替换成活动的目标，比如：被点赞用户的唯一标识");

            // 2.生成签名信息
            String signature = Utils.genSignature(secretPair.secretKey, parameters);
            parameters.Add("signature", signature);

            // 3.发送HTTP请求
            String response = Utils.doPost(client, VERIFY_API, parameters, 5000);
            return verifyRet(response);
        }

        /// <summary>
        /// 解析二次校验接口返回的结果
        /// </summary>
        /// <param name="response"></param>
        /// <returns></returns>
        private VerifyResultType verifyRet(string response)
        {
            VerifyResultType returnresult = VerifyResultType.Success;
            if (String.IsNullOrEmpty(response))
            {
                returnresult = VerifyResultType.Error;
                return returnresult;
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

                    returnresult = (VerifyResultType)actioncode;
                }
                else
                {
                    Console.WriteLine("error: {0}", msg);
                    returnresult = (VerifyResultType)code;
                }
            }
            catch (Exception e)
            {
                if (e.Source != null)
                {
                    Console.WriteLine("IOException source: {0}", e.Source);
                }
                returnresult = VerifyResultType.Error;

            }
            return returnresult;
        }
    }

    /// <summary>
    /// 易盾验证码密钥对
    /// </summary>
    public class NESecretPair
    {
        public string secretId; // 密钥对id
        public string secretKey; // 密钥对key

        public NESecretPair(string secretId, string secretKey)
        {
            this.secretId = secretId;
            this.secretKey = secretKey;
        }
    }
}