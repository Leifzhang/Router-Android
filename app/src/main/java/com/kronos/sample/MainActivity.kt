package com.kronos.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kronos.router.Router
import com.kronos.router.init.RouterInit_app
import com.kronos.router.init.RouterInit_second
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Router.sharedRouter().attachApplication(application)
        injectInit()
        routerTesting.setOnClickListener {
            Router.sharedRouter().open("https://www.baidu.com/test", this)
        }
        routerBaidu.setOnClickListener {
            Router.sharedRouter().open("https://www.baidu.com/test/12345", this)
        }
    }

    private fun injectInit() {
    }
}
