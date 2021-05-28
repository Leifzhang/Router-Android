package com.kronos.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kronos.sample.viewbinding.ext.viewBinding
import com.kronos.router.Router
import com.kronos.router.coroutine.dispatcher
import com.kronos.router.dsl.bundle
import com.kronos.router.dsl.fail
import com.kronos.router.dsl.request
import com.kronos.router.dsl.success
import com.kronos.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding by viewBinding()
    val viewModel :TestViewModel? by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Router.sharedRouter().attachApplication(application)
        Router.addGlobalInterceptor(LogInterceptor())
        Router.map("https://www.baidu.com/test", TestActivity::class.java, LogInterceptor())
        binding.routerTesting.setOnClickListener {
            request("https://www.baidu.com/test") {
                activityResultCode = 12345
                success {

                }
                fail {

                }
                bundle {
                    putString("1234", "1234")
                }
            }.start(this)
        }
        binding.routerBaidu.setOnClickListener {
            GlobalScope.launch {
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
            }
        }

    }


}
