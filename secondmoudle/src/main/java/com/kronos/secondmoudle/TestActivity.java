package com.kronos.secondmoudle;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kronos.router.BindRouter;
import com.kronos.router.interceptor.TestInterceptor;

/**
 * Created by zhangyang on 16/7/16.
 */
@BindRouter(urls = {"https://www.baidu.com/:id{string}/:nid{long}"}, weight = 2, interceptors = {TestInterceptor.class})
public class TestActivity extends AppCompatActivity {
    //  TextView testTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_test);
        String name = getIntent().getStringExtra("string");
        //  testTv.setText(name);
    }

}
