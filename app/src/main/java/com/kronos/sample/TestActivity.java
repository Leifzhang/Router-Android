package com.kronos.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.kronos.router.BindRouter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangyang on 16/7/16.
 */

//@BindRouter(urls = {"https://www.baidu.com/test"}, weight = 10)
public class TestActivity extends Activity {
    @BindView(R.id.testTv)
    TextView testTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);
        ButterKnife.bind(this);
        try {
            String name = getIntent().getStringExtra("string");
            testTv.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
