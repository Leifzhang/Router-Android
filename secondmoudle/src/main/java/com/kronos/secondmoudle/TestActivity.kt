package com.kronos.secondmoudle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kronos.router.BindRouter

/**
 * Created by zhangyang on 16/7/16.
 */
@BindRouter(urls = ["https://www.baidu.com/:id{string}/:nid{long}"], weight = 2, interceptors = [TestInterceptor::class])
class TestActivity : AppCompatActivity() {
    //  TextView testTv;
    @Deprecated("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity_test)
        val name = intent.getStringExtra("string")
        //  testTv.setText(name);
    }
}