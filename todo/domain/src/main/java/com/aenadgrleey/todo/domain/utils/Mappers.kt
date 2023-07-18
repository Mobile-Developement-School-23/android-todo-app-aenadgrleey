package com.aenadgrleey.todo.domain.utils

interface Mapper<I, O> {
    fun map(input: I): O
}