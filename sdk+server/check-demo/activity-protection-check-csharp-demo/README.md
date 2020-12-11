# anticheat-CSharp-demo
易盾反作弊CSharp演示

# demo运行环境
* 开发工具 visual studio 2019 社区版，.Net 版本 4.5.2+

# demo运行步骤
* 修改 ActivityAntiCheatChecker.cs
```
    /** 这里填入在易盾官网申请的产品密钥ID */
    private static string SECRET_ID = "YOUR_SECRET_ID";
    /** 这里填入在易盾官网申请的产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
    private static string SECRET_KEY = "YOUR_SECRET_KEY";
    /** 这里填入在易盾官网申请的业务ID，易盾根据产品业务特点分配 */
    private static string BUSINESS_ID = "YOUR_BUSINESS_ID";
    ...
    /** 这里填入前端提交的查询token */
    string token = "YOUR_FRONT_TOKEN";
```
* 运行 ActivityAntiCheatChecker.cs
