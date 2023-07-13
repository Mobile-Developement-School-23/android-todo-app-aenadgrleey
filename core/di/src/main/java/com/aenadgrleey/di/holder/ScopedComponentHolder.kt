package com.aenadgrleey.di.holder

import androidx.lifecycle.ViewModel

class ScopedComponentHolder<T>(
    val component: T,
) : ViewModel()