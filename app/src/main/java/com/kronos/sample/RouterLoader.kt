package com.kronos.sample

import com.kronos.router.init.RouterInit_app
import com.kronos.router.init.RouterInit_second

/**
 * Created by  Leif Zhang on 2017/8/28.
 * Email leifzhanggithub@gmail.com
 */

class RouterLoader {

    var isLoadingFinish = false
        private set

    fun attach() {
        injectInit()
    }

    fun injectInit() {
        RouterInit_app.init()
        RouterInit_second.init()
    }


}
