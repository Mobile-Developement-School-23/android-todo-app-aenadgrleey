package com.aenadgrleey.tobedone.utils

interface Mapper<I, O> {
    fun map(input: I): O
}