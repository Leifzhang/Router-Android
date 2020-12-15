package com.kronos.sample

import android.app.Activity
import android.os.Bundle
import com.kronos.router.BindRouter
import kotlinx.android.synthetic.main.new_activity.*

/**
 * Created by zhangyang on 16/7/16.
 */
@BindRouter(urls = ["https://www.baidu.com/test"], weight = 10)
class TestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity)
        val name = intent.getStringExtra("string")
        name?.apply { testTv.text = name }
        resultBtn.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

    }
}