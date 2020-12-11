# anticheat-golang-demo

易盾反作弊golang演示

# demo运行环境

* Golang : 1.15.6

# demo运行步骤

* 修改 check.go

```
    /** 这里填入在易盾官网申请的产品密钥ID */
    secretId   = "YOUR_SECRET_ID"
    /** 这里填入在易盾官网申请的产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
	secretKey  = "YOUR_SECRET_KEY"
    /** 这里填入在易盾官网申请的业务ID，易盾根据产品业务特点分配 */
	businessId = "YOUR_BUSINESS_ID"
	...
    params := url.Values{
        // 前端提交的查询token
		"token":  []string{"YOUR_FRONT_TOKEN"},
     }
```

* 运行go run check.go
