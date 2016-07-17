package com.kronos.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kronos.router.Router;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        RouterConstants.init();
    }

    @OnClick({R.id.routerTesting, R.id.routerBaidu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.routerTesting:
                Router.sharedRouter().open("https://github.com/leifzhang", this);
                break;
            case R.id.routerBaidu:
                Router.sharedRouter().open("https://wwww.baidu.com/hellobaidu", this);
                break;
        }
    }
}
