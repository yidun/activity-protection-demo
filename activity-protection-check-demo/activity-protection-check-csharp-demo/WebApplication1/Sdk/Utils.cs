using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net;
using System.Security.Cryptography;
using System.Text;
using System.Web;
using System.Threading.Tasks;

namespace ActivityProtectionDemo.Sdk
{
    public class Utils
    {
        /// <summary>
        /// 根据请求参数生成对应的签名信息
        /// </summary>
        /// <param name="secretKey"></param>
        /// <param name="parameters"></param>
        /// <returns></returns>
        public static String genSignature(String secretKey, Dictionary<String, String> parameters)
        {
            parameters = parameters.OrderBy(o => o.Key).ToDictionary(o => o.Key, p => p.Value);

            StringBuilder builder = new StringBuilder();
            foreach (KeyValuePair<String, String> kv in parameters)
            {
                builder.Append(kv.Key).Append(kv.Value);
            }
            builder.Append(secretKey);
            String tmp = builder.ToString();
            MD5 md5 = new MD5CryptoServiceProvider();
            byte[] result = md5.ComputeHash(Encoding.UTF8.GetBytes(tmp));
            builder.Clear();
            foreach (byte b in result)
            {
                builder.Append(b.ToString("x2").ToLower());
            }
            return builder.ToString();
        }

        public static HttpClient makeHttpClient()
        {
            HttpClient client = new HttpClient() { };
            client.DefaultRequestHeaders.Connection.Add("keep-alive");
            return client;
        }

        /// <summary>
        /// 发起HTTP-Post请求
        /// </summary>
        /// <param name="client">httpClient对象</param>
        /// <param name="url">请求接口url</param>
        /// <param name="parameters">请求参数</param>
        /// <param name="timeOutInMillisecond">超时时间</param>
        /// <returns></returns>
        public static String doPost(HttpClient client, String url, Dictionary<String, String> parameters, int timeOutInMillisecond)
        {
            HttpContent content = new FormUrlEncodedContent(parameters);
            Task<HttpResponseMessage> task = client.PostAsync(url, content);
            if (task.Wait(timeOutInMillisecond))
            {
                HttpResponseMessage response = task.Result;
                if (response.StatusCode == HttpStatusCode.OK)
                {
                    Task<string> result = response.Content.ReadAsStringAsync();
                    result.Wait();
                    return result.Result;
                }
            }
            return null;
        }
    }
}