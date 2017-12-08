活动反作弊 Android sdk 示例 demo
===

### demo 运行步骤

* 1、运行模拟业务后端：check demo，运行方法见activity-protection-check-demo目录

* 2、修改 MainActivity.java 的 onCreate，填入您的 productNumber，如下：
```
     watchman.init(getApplicationContext(), "your productNumber");
```	 
* 3、修改 ActivityTask.java的 doInBackground 方法,在 params.put()中填入您的BusinessId,如下：
```
	 params.put("token", watchman.getToken("your BusinessId"));
```
* 4、修改 ActivityTask.java的 PostData方法中的url变量
```
     String url = "http://localhost:8181/rise.do";
     // 例如，替换如下：
     String url = "http://10.240.132.43:8181/rise.do";
```
* 5、至此，配置和修改完成，编译运行即可