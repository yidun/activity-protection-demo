活动反作弊 Android sdk 示例 demo
===

### demo 运行步骤

* 1、将sdk文件拷贝到libs目录

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
* 3、修改 MainActivity.java的 onClick 方法,填入正确的配置信息,如下：
```
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == mBtRise) {
                new ActivityTask(MainActivity.this, "Secret ID", "BusinessID", "Secret Key").execute();
            }
        }
    };
```
* 4、至此，配置和修改完成，编译运行即可
