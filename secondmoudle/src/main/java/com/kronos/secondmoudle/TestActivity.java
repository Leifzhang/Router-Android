package com.kronos.secondmoudle;

import android.app.Activity;
import android.os.Bundle;

import com.kronos.router.BindRouter;

/**
 * Created by zhangyang on 16/7/16.
 */
@BindRouter({"https://wwww.wallstreetcn.com"})
public class TestActivity extends Activity {
    //  TextView testTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_test);
        String name = getIntent().getStringExtra("string");
        //  testTv.setText(name);
    }

}
