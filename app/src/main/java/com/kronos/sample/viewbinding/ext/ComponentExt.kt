package com.kronos.sample.viewbinding.ext

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kronos.sample.viewbinding.viewbind.*


inline fun <reified T : ViewBinding> Activity.viewBinding() =
    ActivityViewBinding(T::class.java, this)


inline fun <reified T : ViewBinding> Fragment.viewBinding() =
    FragmentViewBinding(T::class.java, this)

inline fun <reified T : ViewBinding> Dialog.viewBinding() =
    DialogViewBinding(T::class.java)

inline fun <reified T : ViewBinding> Dialog.viewBinding(lifecycle: Lifecycle) =
    DialogViewBinding(T::class.java, lifecycle)

inline fun <reified T : ViewBinding> RecyclerView.ViewHolder.viewBinding() =
    ViewHolderViewBinding(T::class.java)

inline fun <reified T : ViewBinding> ViewGroup.viewBinding() = ViewGroupViewBinding(
    T::class.java,
    LayoutInflater.from(context)
)

inline fun <reified T : ViewBinding> ViewGroup.viewBinding(viewGroup: ViewGroup) =
    ViewGroupViewBinding(
        T::class.java,
        LayoutInflater.from(context),
        viewGroup
    )