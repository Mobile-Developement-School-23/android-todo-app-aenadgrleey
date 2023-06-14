package com.aenadgrleey.tobedone.utils

interface Mapper<in I, out O> {
    fun map(input: I): O
}

interface ListMapper<in I, out O> : Mapper<List<I>, List<O>>

class ListMapperImpl<in I, out O>(
    private val mapper: Mapper<I, O>
) : ListMapper<I, O> {
    override fun map(input: List<I>): List<O> =
        input.map(mapper::map)
}