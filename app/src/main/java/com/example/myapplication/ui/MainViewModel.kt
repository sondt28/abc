package com.example.myapplication.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.SeaCreature
import com.example.myapplication.data.model.Shark
import com.example.myapplication.data.repository.SeaCreatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(private val bounds: Pair<Float, Float>) : ViewModel() {
    private val seaCreatureRepository = SeaCreatureRepository(bounds)

    private val _seaCreatures = MutableStateFlow<List<Shark>>(emptyList())
    val seaCreatures: StateFlow<List<Shark>> = _seaCreatures.asStateFlow()

    init {
        startSwimming()
    }

    private fun startSwimming() {
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(1000)
                seaCreatureRepository.updateSeaCreaturesPositions()
            }
        }
    }

    fun addSeaCreature(seaCreature: SeaCreature) {
        seaCreatureRepository.addSeaCreature(seaCreature)
    }
}