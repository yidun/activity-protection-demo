package com.netease.activitydemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.netease.mobsec.rjsb.watchman;
import com.netease.mobsec.rjsb.RequestCallback;

public class MainActivity extends Activity {
    private Button mBtRise;
    public statc String TAG= "TestLog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        watchman.init(mContext, "your productNumber",new RequestCallback(){
            @Override
            public void onResult(int code, String msg) {
                Log.e(TAG,"init,code = " + code + " msg = " + msg);
            }
        });
        setContentView(R.layout.activity_main);

        mBtRise = (Button)findViewById(R.id.bt_rise);
        mBtRise.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == mBtRise){
                new ActivityTask(MainActivity.this){
                }.execute();
            }
        }
    };
}
