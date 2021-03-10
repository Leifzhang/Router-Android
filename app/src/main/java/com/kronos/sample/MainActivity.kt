package com.kronos.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kronos.router.KRequest
import com.kronos.router.Router
import com.kronos.router.coroutine.await
import com.kronos.router.coroutine.dispatcher
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Router.sharedRouter().attachApplication(application)
        Router.addGlobalInterceptor(LogInterceptor())
        Router.map("https://www.baidu.com/test", TestActivity::class.java, LogInterceptor())
        routerTesting.setOnClickListener {
            Router.sharedRouter().open("https://www.baidu.com/test/123", this)
        }
        routerBaidu.setOnClickListener {
            GlobalScope.launch {
                KRequest("https://www.baidu.com/test").apply {
                    activityResultCode = 12345
                    addValue("1234", "1234")
                }.dispatcher(this@MainActivity)
                Log.i("", "")
            }
        }

    }


}
