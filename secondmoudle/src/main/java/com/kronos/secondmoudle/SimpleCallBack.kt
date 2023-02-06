package com.kronos.secondmoudle

import android.widget.Toast
import com.kronos.router.BindRouter
import com.kronos.router.RouterCallback
import com.kronos.router.RouterContext
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Leif Zhang on 2016/12/6.
 * Email leifzhanggithub@gmail.com
 */
@BindRouter(urls = ["https://wwww.baidu.com"], weight = 1, interceptors = [TestInterceptor::class])
class SimpleCallBack : RouterCallback {
    override fun run(context: RouterContext) {
        Toast.makeText(context.context, "testing", Toast.LENGTH_SHORT).show()
    }
}