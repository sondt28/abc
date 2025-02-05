package com.example.myapplication.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.SeaCreature
import com.example.myapplication.data.model.SeaCreatureData
import com.example.myapplication.data.model.Shark
import com.example.myapplication.data.model.TiniTuna
import com.example.myapplication.data.repository.SeaCreatureRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun createRandomSeaCreature(position: Pair<Float, Float>) : SeaCreature {
        val shark = Shark(position = Pair(position.first, position.second))
        seaCreatureRepository.addSeaCreature(shark)

        _uiState.update {
            it.copy(seaCreatures = getSeaCreaturesData())
        }

        return shark
    }

    fun createSpecificSeaCreature(position: Pair<Float, Float>) : SeaCreature {
        val tiniTuna = TiniTuna(position = Pair(position.first, position.second))
        seaCreatureRepository.addSeaCreature(tiniTuna)

        _uiState.update {
            it.copy(seaCreatures = getSeaCreaturesData())
        }

        return tiniTuna
    }

    fun removeAll() {
        seaCreatureRepository.removeAll()
    }
}

data class UiState(val seaCreatures: List<SeaCreatureData>)