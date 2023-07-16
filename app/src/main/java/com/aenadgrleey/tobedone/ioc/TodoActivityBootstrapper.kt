package com.aenadgrleey.tobedone.ioc

import javax.inject.Inject

class TodoActivityBootstrapper @Inject constructor(
    navigationController: NavigationController,
    themeController: ThemeController,
    workerController: WorkerController,
) {
    init {
        navigationController.setUpNavigationControl()
        themeController.setUpThemeControl()
//        workerController.setUpWorkers()
    }
}