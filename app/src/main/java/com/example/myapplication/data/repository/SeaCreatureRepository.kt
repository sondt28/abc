package com.example.myapplication.data.repository

import com.example.myapplication.data.datasource.SeaCreatureDataSource
import com.example.myapplication.data.model.seacreature.SeaCreature
import com.example.myapplication.data.model.seacreature.SeaCreatureData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SeaCreatureRepository(private val bounds: Pair<Float, Float>) {
    private val seaCreatureDataSource = SeaCreatureDataSource()
    private val _seaCreatureListFlow = MutableStateFlow<List<SeaCreature>>(listOf())
    val notifyChangePosition: MutableSharedFlow<Boolean> = MutableSharedFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val flow = seaCreatureDataSource.getSeaCreatures()
        .flowOn(Dispatchers.IO)
        .stateIn(coroutineScope, SharingStarted.Eagerly, listOf())

    fun addSeaCreature(seaCreature: SeaCreature) {
        val currentList = _seaCreatureListFlow.value.toMutableList()
        currentList.add(seaCreature)
        _seaCreatureListFlow.value = currentList

        seaCreatureDataSource.addSeaCreature(seaCreature)

        seaCreature.startSwim(coroutineScope, bounds) {
            coroutineScope.launch {
                seaCreature.detectCollisions(_seaCreatureListFlow.value)
                notifyChangePosition.emit(true)
            }
        }

        seaCreature.onEaten = {
            removeSeaCreature(it)
        }
    }

    fun getSeaCreaturesData(): List<SeaCreatureData> {
        return _seaCreatureListFlow.value.map {
            SeaCreatureData(
                id = it.id,
                size = it.size,
                image = it.imageRes,
                position = it.position,
                velocity = it.velocity
            )
        }
    }

    private fun removeSeaCreature(seaCreature: SeaCreature) {
        seaCreature.stopSwim()
        val currentList = _seaCreatureListFlow.value.toMutableList()
        currentList.remove(seaCreature)
        _seaCreatureListFlow.value = currentList
    }

    fun removeAll() {
        coroutineScope.cancel()
        _seaCreatureListFlow.value = listOf()
        notifyChangePosition.tryEmit(true)
    }
}