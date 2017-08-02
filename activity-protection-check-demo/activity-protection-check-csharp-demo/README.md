# 演示说明

* 开发工具 visual studio 2015 社区版，.Net 版本 4.5.2
* 打开activity-protection-check-csharp-demo.sln
* 右键解决方案，还原NuGet包
* 调整 Views\Activity\Index.cshtml 文件


* 调整 Controllers\ActivityController.cs 文件，填入从易盾官网申请到的配置信息
```
	private static string secretId = "YOUR_SECRET_ID"; // 密钥对id
	private static string secretKey = "YOUR_SECRET_KEY"; // 密钥对key
	private static string businessID = "YOUR_BUSINESS_ID"; // bussinessId
```

* 修改Index.cshtml，替换sdk的配置

    <script type="text/javascript" 
        src="http://nos.netease.com/yidun/res-wm-1.0.js?t=0" 
        pro-data="YOUR_SDK_CONFIGURATION"></script>

* 运行演示程序
* 欢迎fork & pull request帮助改进Demo