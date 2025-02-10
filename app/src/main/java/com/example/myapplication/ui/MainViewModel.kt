package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.seacreature.SeaCreatureData
import com.example.myapplication.data.model.seacreature.SeaCreatureType
import com.example.myapplication.data.repository.SeaCreatureRepository
import com.example.myapplication.util.Const.POSITION
import com.example.myapplication.util.Const.TYPE
import com.example.myapplication.util.SeaCreatureRandomFactory
import com.example.myapplication.util.SeaCreatureSelectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(private val bounds: Pair<Float, Float>) : ViewModel() {
    private val seaCreatureRepository = SeaCreatureRepository(bounds)

    val seaCreaturesFlow = getSeaCreaturesData()
    private val seaCreatureSelectionFactory: SeaCreatureSelectionFactory = SeaCreatureSelectionFactory()
    private val seaCreatureRandomFactory: SeaCreatureRandomFactory = SeaCreatureRandomFactory()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            while (viewModelScope.isActive) {
                delay(16)
                seaCreatureRepository.detectCollisions()
            }
        }
    }

    private fun getSeaCreaturesData(): Flow<List<SeaCreatureData>> {
        return seaCreatureRepository.getSeaCreaturesData()
    }

    fun createSeaCreature(bounds: Pair<Int, Int>, type: SeaCreatureType? = null, onPositionChanged: (SeaCreatureData) -> Unit) {
        val (randomX, randomY) = generateRandomPosition(bounds)

        val seaCreature = if (type != null) {
            seaCreatureSelectionFactory.create(mapOf(TYPE to type, POSITION to Pair(randomX, randomY)))
        } else {
            seaCreatureRandomFactory.create(mapOf(POSITION to Pair(randomX, randomY)))
        }

        seaCreatureRepository.addSeaCreature(seaCreature) {
            onPositionChanged(it)
        }
    }

    private fun generateRandomPosition(bounds: Pair<Int, Int>): Pair<Float, Float> {
        val x = Random.nextInt(0, bounds.first - 200).toFloat()
        val y = Random.nextInt(0, bounds.second - 200).toFloat()

        return Pair(x, y)
    }
}

data class UiState(val seaCreatures: List<SeaCreatureData>)