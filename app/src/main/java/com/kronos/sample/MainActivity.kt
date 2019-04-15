package com.kronos.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.kronos.router.Router

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Router.sharedRouter().attachApplication(application)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.routerTesting, R.id.routerBaidu)
    fun onClick(view: View) {
        when (view.id) {
            R.id.routerTesting -> Router.sharedRouter().open("https://www.baidu.com/test", this)
            R.id.routerBaidu -> Router.sharedRouter().open("https://www.baidu.com/test/12345", this)
        }
    }
}
