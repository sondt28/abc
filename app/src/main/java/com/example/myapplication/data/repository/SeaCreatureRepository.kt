package com.example.myapplication.data.repository

import com.example.myapplication.data.model.SeaCreature
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SeaCreatureRepository(private val bounds: Pair<Float, Float>) {
    private val seaCreatureListFlow = MutableStateFlow<ArrayList<SeaCreature>>(arrayListOf())

    fun getSeaCreatureList() = seaCreatureListFlow.asStateFlow()

    fun addSeaCreature(seaCreature: SeaCreature) {
        seaCreatureListFlow.value.add(seaCreature)
    }

    suspend fun updateSeaCreaturesPositions() {
        delay(
            2000
        )
        seaCreatureListFlow.value.forEach {
            it.swimming(bounds)
        }
    }

    fun removeSeaCreature(seaCreatureId: Long) {

    }
}