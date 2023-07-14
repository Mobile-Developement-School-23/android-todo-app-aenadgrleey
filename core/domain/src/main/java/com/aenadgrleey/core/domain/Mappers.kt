package com.aenadgrleey.core.domain

interface Mapper<I, O> {
    fun map(input: I): O
}