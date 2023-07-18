package com.aenadgrleey.tobedone.di.broadcast

import com.aenadgrleey.todonotify.ui.notify.TaskDeadlineNotificator
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {
    @ContributesAndroidInjector
    abstract fun contributesMyTestReceiver(): TaskDeadlineNotificator
}