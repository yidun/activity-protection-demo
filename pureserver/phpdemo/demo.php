<?php

/** 产品密钥ID，产品标识 */
define("SECRETID", "YOUR_SECRET_ID");
/** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
define("SECRETKEY", "YOUR_SECRET_KEY");
/** 业务ID，易盾根据产品业务特点分配 */
define("BUSINESSID", "YOUR_BUSINESS_ID");

/** 易盾活动反作弊在线检测接口地址 */
define("API_URL", "https://ac.dun.163yun.com/pureserver/activity/check");
/** api version */
define("VERSION", "200");
/** API timeout*/
define("API_TIMEOUT", 1);
/** php内部使用的字符串编码 */
define("INTERNAL_STRING_CHARSET", "auto");

/**
 * 计算参数签名
 * $params 请求参数
 * $secretKey secretKey
 */
function gen_signature($secretKey, $params){
    ksort($params);
    $buff="";
    foreach($params as $key=>$value){
        $buff .=$key;
        $buff .=$value;
    }
    $buff .= $secretKey;
    return md5($buff);
}

/**
 * 将输入数据的编码统一转换成utf8
 * @params 输入的参数
 * @inCharset 输入参数对象的编码
 */
function toUtf8($params){
    $utf8s = array();
    foreach ($params as $key => $value) {
      $utf8s[$key] = is_string($value) ? mb_convert_encoding($value, "utf8",INTERNAL_STRING_CHARSET) : $value;
    }
    return $utf8s;
}

function convert_null($params) {
    $new_params = array();
    foreach ($params as $key => $value) {
        $new_params[$key] = is_null($value) ? '' : $value;
    }
    return $new_params;
}

/**
 * 活动反作弊检测接口简单封装
 * $params 请求参数
 */
function check($params){
    $params["secretId"] = SECRETID;
    $params["businessId"] = BUSINESSID;
    $params["version"] = VERSION;
    $params["timestamp"] = time();
    $params["nonce"] = sprintf("%d", rand()); // random int

    $params = convert_null($params);
    $params = toUtf8($params);
    $params["signature"] = gen_signature(SECRETKEY, $params);
    // print_r($params);

    $options = array(
        'http' => array(
            'header'  => "Content-type: application/x-www-form-urlencoded\r\n",
            'method'  => 'POST',
            'timeout' => API_TIMEOUT, // read timeout in seconds
            'content' => http_build_query($params),
        ),
    );
    $context  = stream_context_create($options);
    $result = file_get_contents(API_URL, false, $context);
    return json_decode($result, true);
}

// 简单测试
function main(){
	$params = array();
	if (isset($_POST['token'])) {
		$params['token'] = $_POST['token'];
	}

    //以下在纯服务端是必传参数
    $params['account'] = '请替换成用户的唯一标识';
    $params['ip'] = '请替换成用户点赞使用的IP';
    $params['runEnv'] = '请替换成用户客户端运行环境,取值可以查看接入文档列表';
    $params['operatingTime'] = '请替换成用户操作时间';
    
    // 以下是可选参数，如果有，建议提供，数据越多，识别效果越好
    /*
        $params['email'] = '请替换成用户的邮箱';
        $params['phone'] = '请替换成用户的手机号';
        $params['registerTime'] = '请替换成用户注册的时间（单位：秒）';
        $params['registerIp'] = '请替换成用户注册时用的IP';
    	$params["activityId"] = "请替换成活动的唯一标识";
        $params["target"] = "请替换成活动的目标，比如：被点赞用户的唯一标识";
        $params['sessionIdHash'] = '请替换成用户sessionId的hash值'; 
        $params['referrer'] = '请替换成用户http请求的Referrer头'; 
        $params['userAgent'] = '请替换成用户http请求的UserAgent'; 
        $params['xForwardFor'] = '请替换成用户http请求的X-Forwarded-For头信息'; 
        $params['moveCount'] = '请替换成用户操作过程中的move类事件的次数'; 
	    $params['clickCount'] = '请替换成用户操作过程中鼠标点击次数'; 
        $params['keyCount'] =  '请替换成用户操作过程中键盘的摁键次数'; 
        $params['mac'] =  '请替换成用户设备的MAC地址'; 
        $params['imei'] =  '请替换成用户设备的移动终端设备码'; 
        $params['imsi'] =  '请替换成用户设备的移动用户识别码'; 
        $params['appVersion'] =  '请替换成APP版本号'; 
        $params['model'] =  '请替换成移动设备的Model'; 
        $params['androidId'] =  '请替换成Android设备的AndroidId'; 
        $params['serialNum'] =  '请替换成设备的序列号'; 
        $params['extData'] =  '扩展字段，json格式的字符串，请与安全策略经理协商数据格式
'; 
    */
	
    try {
        $ret = check($params); 
        if ($ret["code"] == 200) {
            $action = $ret["result"]["action"];
            $hitType = $ret["result"]["hitType"];
            if ($action == 0) {
                echo "正常";
            } else if ($action == 10) {
                echo "有作弊的嫌疑";
            } else if ($action == 20) {
                echo "有明显的作弊特征";
            }
        }else{
			// 接口调用出现错误
			// 请根据错误码 和 错误消息分析原因
			echo "接口调用出错，请根据错误码和错误消息分析原因<br />";
            var_dump($ret);
        }
    } catch (Exception $e) {
        echo "接口调用异常（超时 等），当作[正常]处理";
    }
}

main();
?>