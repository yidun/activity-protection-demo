package com.netease.activitydemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.netease.mobsec.InitCallback;
import com.netease.mobsec.WatchMan;
import com.netease.mobsec.WatchManConf;

import static java.lang.Boolean.TRUE;

public class MainActivity extends Activity {
    private Button mBtRise;
    public static String TAG = "TestLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WatchManConf myconf = new WatchManConf();
        myconf.setCollectApk(TRUE);
        myconf.setCollectSensor(TRUE);
        WatchMan.init(getApplicationContext(), "Product Number", myconf, new InitCallback() {
            @Override
            public void onResult(int code, String msg) {
                Log.e(TAG, "init OnResult , code = " + code + " msg = " + msg);
            }
        });
        setContentView(R.layout.activity_main);

        mBtRise = (Button) findViewById(R.id.bt_rise);
        mBtRise.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == mBtRise) {
                new ActivityTask(MainActivity.this, "Secret ID", "BusinessID", "Secret Key").execute();
            }
        }
    };
}
