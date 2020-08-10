活动反作弊 Android sdk 示例 demo
===

### demo 运行步骤

* 1、运行模拟业务后端：check demo，运行方法见activity-protection-check-demo目录

* 2、修改 MainActivity.java 的 onCreate，填入您的 productNumber，如下：
```
        WatchManConf myconf = new WatchManConf();
        myconf.setCollectApk(TRUE);
        myconf.setCollectSensor(TRUE);
        WatchMan.init(getApplicationContext(), "ProductNumber", myconf, new InitCallback() {
            @Override
            public void onResult(int code, String msg) {
                Log.e(TAG,"init OnResult , code = " + code + " msg = " + msg);
            }
        });
```	 
* 3、修改 ActivityTask.java的 doInBackground 方法,在 params.put()中填入您的BusinessId,如下：
```
        WatchMan.getToken(new GetTokenCallback(){
            @Override
            public void onResult(int code, String msg,String Token) {
                Log.e(TAG,"Register, code = " + code + " msg = " + msg+" Token:"+Token);

                Map<String, String> params = new HashMap<String, String>();
                params.put("token", Token);
                PostData(params);

            }
        });
```
* 4、修改 ActivityTask.java的 PostData方法中的url变量
```
     String url = "http://localhost:8181/rise.do";
     // 例如，替换如下：
     String url = "http://10.240.132.43:8181/rise.do";
```
* 5、至此，配置和修改完成，编译运行即可
