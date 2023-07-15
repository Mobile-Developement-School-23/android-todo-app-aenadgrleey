package com.aenadgrleey.tobedone.ioc

import javax.inject.Inject

class TodoActivityBootstrapper @Inject constructor(
    navigationController: NavigationController,
    workerController: WorkerController,
) {
    init {
        navigationController.setUpNavigation()
//        workerController.setUpWorkers()
    }
}