package com.example.myapplication.data.model

interface SeaCreatureFactory {
    fun create() : SeaCreature
}

class SeaCreatureSelectionFactory(private val type: String) : SeaCreatureFactory {
    override fun create(): SeaCreature {
        TODO("Not yet implemented")
    }
}

class SeaCreatureRandomFactory : SeaCreatureFactory {
    override fun create(): SeaCreature {
        TODO("Not yet implemented")
    }
}