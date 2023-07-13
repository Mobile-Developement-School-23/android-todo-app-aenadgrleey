package com.aenadgrleey.di.holder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

internal class ScopedComponentProperty<T>(
    private val storeOwner: ViewModelStoreOwner,
    private val componentProvider: () -> T,
) : Lazy<T> {

    private var cached: T? = null

    override val value: T
        get() {
            val component = cached
            if (component != null) return component
            val viewModels = ViewModelProvider(
                storeOwner,
                object : ViewModelProvider.Factory {
                    override fun <VM : ViewModel> create(modelClass: Class<VM>) =
                        ScopedComponentHolder(componentProvider()) as VM
                }
            )
            val componentHolder = viewModels[ScopedComponentHolder::class.java]
            return (componentHolder.component as T).also { cached = it }
        }

    override fun isInitialized() = cached != null
}