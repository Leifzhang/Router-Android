package com.kronos.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kronos.sample.viewbinding.ext.viewBinding
import com.kronos.router.Router
import com.kronos.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by viewBinding()
    val viewModel: TestViewModel? by viewModels()
    val handler =
        CoroutineExceptionHandler { coroutineContext: CoroutineContext, throwable: Throwable ->

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Router.sharedRouter().attachApplication(application)
        Router.addGlobalInterceptor(LogInterceptor())
        val processName = Test.getProcessName(this)
        Router.map("https://www.baidu.com/test", TestActivity::class.java, LogInterceptor())
        /*     NotificationSample.show(this).build().notify(this, BUSINESS.MAIN)
             NotificationSample.show(this).build().notify(this, BUSINESS.MAIN)
             NotificationSample.show(this).build().notify(this, BUSINESS.MAIN)
             NotificationSample.show(this).build().notify(this, BUSINESS.GAME)
             NotificationSample.show(this).build().notify(this, BUSINESS.GAME)
             NotificationSample.show(this).build().notify(this, BUSINESS.GAME)
             NotificationSample.show(this).build().notify(this, BUSINESS.GAME)
             NotificationSample.show(this).build().notify(this, BUSINESS.GAME)
             NotificationSample.show(this).build().notify(this, BUSINESS.GAME)*/
        startService(Intent(this, TestService::class.java))
        binding.routerTesting.setOnClickListener {
            finish()
            stopService(Intent(this, TestService::class.java))
            // NotificationSample.getNotificationManager(this@MainActivity)?.cancelAll(BUSINESS.MAIN)
            /*    request("https://www.baidu.com/test") {
                    bundle {
                        putString("1234", "1234")
                    }
                    //activityResultCode = 12345
                    fail {

                    }
                    success {

                    }
                }.start(this)*/
        }
        binding.routerBaidu.setOnClickListener {
            NotificationSample.getNotificationManager(it.context)?.cancelAll()
            /*GlobalScope.launch(Dispatchers.Main + handler) {
                val result = request("https://www.baidu.com/test") {
                    activityResultCode = 12345
                    bundle {
                        putString("1234", "1234")
                    }
                }.dispatcher(this@MainActivity)
                delay(1000)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        if (result) "成功了" else "失败了",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }*/
        }

    }


}
