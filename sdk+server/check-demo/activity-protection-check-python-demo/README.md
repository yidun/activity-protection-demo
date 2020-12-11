# anticheat-python-demo

易盾反作弊python示例代码

# demo运行环境

* python版本：python3.7

# demo运行步骤

* 修改 check.py

```
    # 这里填入在易盾官网申请的产品密钥ID
    SECRET_ID = "YOUR_SECRET_ID"
    # 这里填入在易盾官网申请的产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
    SECRET_KEY = "YOUR_SECRET_KEY"
    # 这里填入在易盾官网申请的业务ID，易盾根据产品业务特点分配
    BUSINESS_ID = "YOUR_BUSINESS_ID" 
    ...
     params = {
        # 前端提交的查询token
        "token": "YOUR_FRONT_TOKEN"
     }
```

* 运行python check.py
