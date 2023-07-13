package com.aenadgrleey.datasources.di

import android.content.Context
import com.aenadgrleey.data.remote.AuthToken
import com.aenadgrleey.di.AppContext

interface DataDeps {
    @AuthToken
    val token: String

    @AppContext
    val context: Context
}