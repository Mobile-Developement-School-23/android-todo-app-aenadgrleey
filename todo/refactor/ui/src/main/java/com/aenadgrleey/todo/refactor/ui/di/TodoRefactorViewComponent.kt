package com.aenadgrleey.todo.refactor.ui.di

import com.aenadgrleey.todo.refactor.ui.TodoRefactorFragment
import dagger.Subcomponent

@Subcomponent
interface TodoRefactorViewComponent {
    fun inject(todoRefactorFragment: TodoRefactorFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoRefactorViewComponent
    }
}

interface TodoRefactorViewComponentProvider {
    fun todoRefactorViewComponentProvider(): TodoRefactorViewComponent
}