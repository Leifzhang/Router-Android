package com.kronos.sample

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/5
 *
 */
class TestService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

      //  NotificationSample.show(this, "NotifyInService").build().notify(this, BUSINESS.MAIN)
    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationSample.show(this, "NotifyInService").build().notify(this, BUSINESS.MAIN)

    }
}