package com.example.myapplication.data.datasource

import android.util.Log
import com.example.myapplication.data.model.seacreature.SeaCreature
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class SeaCreatureDataSource {
    private val seaCreatures = mutableListOf<SeaCreature>()
    private val _seaCreaturesFlow = MutableSharedFlow<List<SeaCreature>>(replay = 1)

    fun getSeaCreatures(): Flow<List<SeaCreature>> = _seaCreaturesFlow

    fun addSeaCreature(seaCreature: SeaCreature) {
        seaCreatures.add(seaCreature)
        _seaCreaturesFlow.tryEmit(seaCreatures)
    }

    fun removeSeaCreature(seaCreature: SeaCreature) {
        seaCreatures.remove(seaCreature)
        _seaCreaturesFlow.tryEmit(seaCreatures)
    }
}