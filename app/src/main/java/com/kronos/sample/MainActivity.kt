package com.kronos.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kronos.router.KRequest
import com.kronos.router.Router
import kotlinx.android.synthetic.main.activity_main.*

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
            try {
                val request = KRequest("https://www.baidu.com/test", onSuccess = {
                    Log.i("KRequest", "onSuccess")
                }, onFail = {
                    Log.i("KRequest", "onFail")
                }).apply {
                    activityResultCode = 12345
                }.start(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}
