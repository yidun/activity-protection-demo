## 活动反作弊 check端示例demo（php）

check端即 接入方业务后台，该demo仅提供rise.php接口，需要结合sdk demo一起使用

---

## 运行

- 修改 rise.php
```
	/** 产品密钥ID，产品标识 */
	define("SECRETID", "YOUR_SECRET_ID");
	
	/** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
	define("SECRETKEY", "YOUR_SECRET_KEY");
	
	/** 业务ID，易盾根据产品业务特点分配 */
	define("BUSINESSID", "YOUR_BUSINESS_ID");
```

- php -S 127.0.0.1:8182

- 访问 http://127.0.0.1:8182/rise.php

- 结合sdk demo，查看活动反作弊检测效果
