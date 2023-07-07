package com.aenadgrleey.tobedone.utils

import com.google.gson.annotations.SerializedName

enum class Importance {
    @SerializedName("low")
    Low,

    @SerializedName("basic")
    Common,

    @SerializedName("important")
    High
}