package com.kronos.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kronos.router.Router
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Router.sharedRouter().attachApplication(application)
        Router.map("https://www.baidu.com/test", TestActivity::class.java, Bundle(), LogInterceptor())
        routerTesting.setOnClickListener {
            Router.sharedRouter().open("https://www.baidu.com/test", this)
        }
        routerBaidu.setOnClickListener {
            try {
                Router.sharedRouter().open("https://www.baidu.com/test/12345", this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
