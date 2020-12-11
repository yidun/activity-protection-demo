# anticheat-java-demo
易盾反作弊java演示

# demo运行步骤
* jdk: jdk8+ maven:3.3+

# demo运行步骤
* 修改index.html
```
    var wmInstance = null;
    // 初始化SDK，只需初始化一次
    initWatchman({
      productNumber: 'YOUR_PRODUCT_NUMBER',// <-- 这里填入在易盾官网申请的产品编号
      onload: function (instance) {
        wmInstance = instance;
      }
    });
    ...
    var rise = function () {
      if (wmInstance) {
        wmInstance.getToken('YOUR_BUSINESS_ID', // <-- 这里填入在易盾官网申请的业务ID
        function (token) {
          ...
          });
        });
      };
    };
```

* 修改 RiseServlet.java
```
    /** 这里填入在易盾官网申请的产品密钥ID */
    private static final String SECRET_ID = "YOUR_SECRET_ID";

    /** 这里填入在易盾官网申请的产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
    private static final String SECRET_KEY = "YOUR_SECRET_KEY";

    /** 这里填入在易盾官网申请的业务ID，易盾根据产品业务特点分配 */
    private static final String BUSINESS_ID = "YOUR_BUSINESS_ID";
```

* `mvn tomcat7:run`
* 使用浏览器打开index.html 查看演示
