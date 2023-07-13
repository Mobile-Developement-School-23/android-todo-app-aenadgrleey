package com.aenadgrleey.di.holder

import androidx.lifecycle.ViewModelStoreOwner

fun <T> ViewModelStoreOwner.scopedComponent(
    componentProvider: () -> T,
): Lazy<T> {
    return ScopedComponentProperty(this, componentProvider)
}