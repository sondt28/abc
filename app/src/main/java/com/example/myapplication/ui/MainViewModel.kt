package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.seacreature.SeaCreatureData
import com.example.myapplication.data.model.seacreature.SeaCreatureType
import com.example.myapplication.data.repository.SeaCreatureRepository
import com.example.myapplication.util.SeaCreatureFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(private val bounds: Pair<Float, Float>) : ViewModel() {
    private val seaCreatureRepository = SeaCreatureRepository(bounds)

    private val _uiState = MutableStateFlow(UiState(seaCreatures = getSeaCreaturesData()))
    val uiState = _uiState.asStateFlow()

    init {
        collectSeaCreatures()
    }

    private fun collectSeaCreatures() {
        viewModelScope.launch {
            seaCreatureRepository.notifyChangePosition.collect { creatures ->
                _uiState.update { it.copy(seaCreatures = getSeaCreaturesData()) }
            }
        }
    }

    private fun getSeaCreaturesData(): List<SeaCreatureData> {
        return seaCreatureRepository.getSeaCreaturesData()
    }

    fun createSeaCreature(bounds: Pair<Int, Int>){
        val (randomX, randomY) = generateRandomPosition(bounds)

        val seaCreature = SeaCreatureFactory.create(Pair(randomX, randomY))
        seaCreatureRepository.addSeaCreature(seaCreature)

        _uiState.update {
            it.copy(seaCreatures = getSeaCreaturesData())
        }
    }

    fun createSeaCreature(bounds: Pair<Int, Int>, type: SeaCreatureType) {
        val (randomX, randomY) = generateRandomPosition(bounds)
        val seaCreature = SeaCreatureFactory.create(type, Pair(randomX, randomY))
        seaCreatureRepository.addSeaCreature(seaCreature)

        _uiState.update {
            it.copy(seaCreatures = getSeaCreaturesData())
        }
    }

    private fun generateRandomPosition(bounds: Pair<Int, Int>): Pair<Float, Float> {
        val x = Random.nextInt(0, bounds.first - 200).toFloat()
        val y = Random.nextInt(0, bounds.second - 200).toFloat()

        return Pair(x, y)
    }
}

data class UiState(val seaCreatures: List<SeaCreatureData>)