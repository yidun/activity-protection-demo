package com.netease.activitydemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.netease.mobrcsec.rjsb.RjsbHandler;


public class MainActivity extends Activity {
    private Button mBtRise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RjsbHandler.setBusinessId(getApplicationContext(), "YOUR_BUSINESS_ID");
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
