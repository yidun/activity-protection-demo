#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
易盾反作弊python示例代码
接口文档: https://support.dun.163.com/documents/15588071870066688?docId=429073890435571712
python版本：python3.7
"""
__author__ = 'yidun-dev'
__date__ = '2020/12/11'
__version__ = '0.1'

import hashlib
import time
import random
import urllib.request as urlrequest
import urllib.parse as urlparse
import json


class CheckAPIDemo(object):
    """在线检测接口示例代码"""

    API_URL = "https://ac.dun.163.com/v3/common/check"
    VERSION = "300"

    def __init__(self, secret_id, secret_key, business_id):
        """
        Args:
            secret_id (str) 产品密钥ID，产品标识
            secret_key (str) 产品私有密钥，服务端生成签名信息使用
            business_id (str) 业务ID，易盾根据产品业务特点分配
        """
        self.secret_id = secret_id
        self.secret_key = secret_key
        self.business_id = business_id

    def gen_signature(self, params=None):
        """生成签名信息
        Args:
            params (object) 请求参数
        Returns:
            参数签名md5值
        """
        buff = ""
        for k in sorted(params.keys()):
            buff += str(k) + str(params[k])
        buff += self.secret_key
        return hashlib.md5(buff.encode("utf8")).hexdigest()

    def check(self, params):
        """请求易盾接口
        Args:
            params (object) 请求参数
        Returns:
            请求结果，json格式
        """
        params["secretId"] = self.secret_id
        params["businessId"] = self.business_id
        params["version"] = self.VERSION
        params["timestamp"] = int(time.time())
        params["nonce"] = int(random.random() * 100000000)
        params["signature"] = self.gen_signature(params)

        try:
            params = urlparse.urlencode(params).encode("utf8")
            request = urlrequest.Request(self.API_URL, params)
            content = urlrequest.urlopen(request, timeout=1).read()
            return json.loads(content)
        except Exception as ex:
            print("调用API接口失败:", str(ex))


if __name__ == "__main__":
    """示例代码入口"""
    SECRET_ID = "YOUR_SECRET_ID"  # 产品密钥ID，产品标识
    SECRET_KEY = "YOUR_SECRET_KEY"  # 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
    BUSINESS_ID = "YOUR_BUSINESS_ID"  # 业务ID，易盾根据产品业务特点分配
    api = CheckAPIDemo(SECRET_ID, SECRET_KEY, BUSINESS_ID)

    params = {
        "token": "YOUR_FRONT_TOKEN",  # 前端提交的查询token
        # "account": "请替换成用户的唯一标识",
        # "email": "请替换成用户的邮箱",
        # "phone": "请替换成用户的手机号",
        # "ip": "请替换成用户使用的IP",
        # "registerTime": "请替换成用户注册的时间（单位：秒）",
        # "registerIp": "请替换成用户注册时使用的ip",
        # "activityId": "请替换成活动的唯一标识",
        # "target": "请替换成活动的目标，比如：被点赞用户的唯一标识",
        # "nickname": "请替换成用户昵称，比如：昵称",
        # "userLevel": "请替换成用户等级，比如：VIP用户",
        # "extData": "附加数据，json格式"
    }

    ret = api.check(params)

    code: int = ret["code"]
    msg: str = ret["msg"]
    if code == 200:
        result: dict = ret["result"]
        action: int = result["action"]
        taskId: str = result["taskId"]

        if action == 0:
            print("taskId: %s, 正常（放行）" % taskId)
        elif action == 10:
            print("taskId: %s, 正常（观察）" % taskId)
        elif action == 20:
            print("taskId: %s, 致命（拦截）" % taskId)
    else:
        print("ERROR: code=%s, msg=%s" % (ret["code"], ret["msg"]))
