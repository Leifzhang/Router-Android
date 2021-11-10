package com.kronos.sample

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alibaba.fastjson.JSON
import java.lang.reflect.Type

/**
 *
 *  @Author LiABao
 *  @Since 2021/5/30
 *
 */
class TestFragment : Fragment() {

    val viewModel: TestViewModel by viewModels()

    private inline fun <reified T> parse(text: String?): T? {
        try {
            return JSON.parseObject(text, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}