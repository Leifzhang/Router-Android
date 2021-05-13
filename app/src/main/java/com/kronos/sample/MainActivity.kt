package com.kronos.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kronos.router.Router
import com.kronos.router.coroutine.await
import com.kronos.router.coroutine.dispatcher
import com.kronos.router.dsl.bundle
import com.kronos.router.dsl.fail
import com.kronos.router.dsl.request
import com.kronos.router.dsl.success
import com.kronos.sample.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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
