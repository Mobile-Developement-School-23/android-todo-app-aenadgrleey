package com.aenadgrleey.tobedone.utils

interface TodoRefactorFragmentNavigator {

    fun toCalendar(onSuccess: (Long) -> Unit): Unit
    fun navigateUp(): Unit
}