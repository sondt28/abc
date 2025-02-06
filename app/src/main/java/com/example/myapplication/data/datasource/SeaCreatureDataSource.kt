package com.example.myapplication.data.datasource

import com.example.myapplication.data.model.seacreature.SeaCreature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SeaCreatureDataSource {
    private val seaCreatures = mutableListOf<SeaCreature>()

    fun getSeaCreatures(): Flow<List<SeaCreature>> = flow {
        emit(seaCreatures)
        delay(5000)
    }.flowOn(Dispatchers.Default)

    fun addSeaCreature(seaCreature: SeaCreature) {
        seaCreatures.add(seaCreature)
    }

    fun removeSeaCreature(seaCreature: SeaCreature) {
        seaCreatures.remove(seaCreature)
    }
}