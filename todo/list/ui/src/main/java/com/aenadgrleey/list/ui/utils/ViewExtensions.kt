package com.aenadgrleey.list.ui.utils

import android.view.View

fun View.animateVisibility(visibility: Visibility) {
    when (visibility) {
        Visibility.Visible -> {
            this.visibility = View.VISIBLE
            this.animate().alpha(1f).duration = 300L
        }

        Visibility.Invisible -> {
            this.animate().alpha(0f).setDuration(100L)
                .withEndAction { this.visibility = View.GONE }
        }
    }
}

enum class Visibility {
    Visible, Invisible
}