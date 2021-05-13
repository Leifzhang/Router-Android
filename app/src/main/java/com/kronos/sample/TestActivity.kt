package com.kronos.sample

import android.app.Activity
import android.os.Bundle
import com.kronos.router.BindRouter
import com.kronos.sample.databinding.NewActivityBinding

/**
 * Created by zhangyang on 16/7/16.
 */
@BindRouter(urls = ["https://www.baidu.com/test"], weight = 10)
class TestActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = NewActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val name = intent.getStringExtra("string")
        name?.apply { binding.testTv.text = name }
        binding.resultBtn.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

    }
}