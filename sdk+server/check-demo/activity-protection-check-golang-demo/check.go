/*
@Author : yidun_dev
@Date : 2020-12-10
@File : check.go
@Version : 1.0
@Golang : 1.15.6
@Doc : https://support.dun.163.com/documents/15588071870066688?docId=429073890435571712
*/
package main

import (
	"crypto/md5"
	"encoding/hex"
	"fmt"
	simplejson "github.com/bitly/go-simplejson"
	"io/ioutil"
	"math/rand"
	"net/http"
	"net/url"
	"sort"
	"strconv"
	"strings"
	"time"
)

const (
	apiUrl     = "https://ac.dun.163.com/v3/common/check"
	version    = "300"
	secretId   = "YOUR_SECRET_ID"   //产品密钥ID，产品标识
	secretKey  = "YOUR_SECRET_KEY"  //产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
	businessId = "YOUR_BUSINESS_ID" //业务ID，易盾根据产品业务特点分配
)

//请求易盾接口
func check(params url.Values) *simplejson.Json {
	params["secretId"] = []string{secretId}
	params["businessId"] = []string{businessId}
	params["version"] = []string{version}
	params["timestamp"] = []string{strconv.FormatInt(time.Now().UnixNano()/1000000000, 10)}
	params["nonce"] = []string{strconv.FormatInt(rand.New(rand.NewSource(time.Now().UnixNano())).Int63n(10000000000), 10)}
	params["signature"] = []string{genSignature(params)}

	resp, err := http.Post(apiUrl, "application/x-www-form-urlencoded", strings.NewReader(params.Encode()))

	if err != nil {
		fmt.Println("调用API接口失败:", err)
		return nil
	}

	defer resp.Body.Close()

	contents, _ := ioutil.ReadAll(resp.Body)
	result, _ := simplejson.NewJson(contents)
	return result
}

//生成签名信息
func genSignature(params url.Values) string {
	var paramStr string
	keys := make([]string, 0, len(params))
	for k := range params {
		keys = append(keys, k)
	}
	sort.Strings(keys)
	for _, key := range keys {
		paramStr += key + params[key][0]
	}
	paramStr += secretKey

	md5Reader := md5.New()
	md5Reader.Write([]byte(paramStr))
	return hex.EncodeToString(md5Reader.Sum(nil))

}

func main() {
	params := url.Values{
		"token":  []string{"YOUR_FRONT_TOKEN"}, // 前端提交的查询token
		// "account": []string{"请替换成用户的唯一标识"},
		// "email": []string{"请替换成用户的邮箱"},
		// "phone": []string{"请替换成用户的手机号"},
		// "ip": []string{"请替换成用户使用的IP"},
		// "registerTime": []string{"请替换成用户注册的时间（单位：秒）"},
		// "registerIp": []string{"请替换成用户注册时使用的ip"},
		// "activityId": []string{"请替换成活动的唯一标识"},
		// "target": []string{"请替换成活动的目标，比如：被点赞用户的唯一标识"},
		// "nickname": []string{"请替换成用户昵称，比如：昵称"},
		// "userLevel": []string{"请替换成用户等级，比如：VIP用户"},
		// "extData": []string{"附加数据，json格式"},
	}

	ret := check(params)

	code, _ := ret.Get("code").Int()
	message, _ := ret.Get("msg").String()
	if code == 200 {
		result := ret.Get("result")
		action, _ := result.Get("action").Int()
		taskId, _ := result.Get("taskId").String()

		if action == 0 {
			fmt.Printf("taskId: %s, 正常（放行）", taskId)
		} else if action == 10 {
			fmt.Printf("taskId: %s,  正常（观察）", taskId)
		} else if action == 20 {
			fmt.Printf("taskId: %s, 致命（拦截）", taskId)
		}
	} else {
		fmt.Printf("ERROR: code=%d, msg=%s", code, message)
	}
}