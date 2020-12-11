# anticheat-php-demo
易盾反作弊php演示

# demo运行步骤
* php5+ 

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

* 修改 check.php
```
    /** 这里填入在易盾官网申请的产品密钥ID */
    define("SECRETID", "YOUR_SECRET_ID");
    /** 这里填入在易盾官网申请的产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
    define("SECRETKEY", "YOUR_SECRET_KEY");
    /** 这里填入在易盾官网申请的业务ID，易盾根据产品业务特点分配 */
    define("BUSINESSID", "YOUR_BUSINESS_ID");
```

* 将index.html和check.php到web服务器目录，例如可以使用wamp，放入www目录
* 使用浏览器访问index 查看演示
