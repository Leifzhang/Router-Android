package com.kronos.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

    @OnClick(R.id.routerTesting)
    public void onClick() {
        Router.sharedRouter().open("https://github.com/leifzhang", this);
    }
}
