package com.aenadgrleey.tobedone.ioc

import javax.inject.Inject

class TodoActivityBootstrapper @Inject constructor(
    navigationController: NavigationController,
) {
    init {
        navigationController.setUpNavigation()
    }
}