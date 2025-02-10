package com.example.myapplication.data.repository

import com.example.myapplication.data.model.seacreature.SeaCreature
import com.example.myapplication.data.model.seacreature.SeaCreatureData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.math.pow
import kotlin.math.sqrt

class SeaCreatureRepository(private val bounds: Pair<Float, Float>) {
    private val _seaCreaturesFlow = MutableStateFlow(emptyList<SeaCreature>())
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun addSeaCreature(seaCreature: SeaCreature, onPositionChanged: (SeaCreatureData) -> Unit) {
        _seaCreaturesFlow.value += seaCreature
        seaCreature.startSwim(coroutineScope, bounds)

        seaCreature.onPositionChanged = {
            onPositionChanged(
                it.toSeaCreatureData()
            )
        }

        seaCreature.onEaten = {
            removeSeaCreature(it)
        }
    }

    fun detectCollisions() {
        val seaCreatures = _seaCreaturesFlow.value
        if (seaCreatures.size < 2) return

        for (i in seaCreatures.indices) {
            for (j in i + 1 until seaCreatures.size) {
                val creature1 = seaCreatures[i]
                val creature2 = seaCreatures[j]
                val distance = calculateDistance(creature1, creature2)
                val deadRange = (creature1.size + creature2.size) / 2
                val warningRange = (creature1.size + creature2.size)

                when {
                    distance < deadRange -> handlePredation(creature1, creature2)
                    distance < warningRange -> handleAvoidanceOrChase(creature1, creature2)
                }
            }
        }

    }

    private fun calculateDistance(a: SeaCreature, b: SeaCreature): Double {
        return sqrt(
            (a.position.first - b.position.first).toDouble().pow(2) +
                    (a.position.second - b.position.second).toDouble().pow(2)
        )
    }

    private fun handlePredation(a: SeaCreature, b: SeaCreature) {
        val (bigger, smaller) = if (a.size > b.size) a to b else b to a

        bigger.eating(smaller)
        removeSeaCreature(smaller)
    }

    private fun handleAvoidanceOrChase(a: SeaCreature, b: SeaCreature) {
        val (bigger, smaller) = if (a.size > b.size) a to b else b to a

        if (bigger.canEatOther && smaller.size < bigger.size) {
            smaller.escapeFrom(bigger)
        }
    }

    fun getSeaCreaturesData(): Flow<List<SeaCreatureData>> {
        return _seaCreaturesFlow.map { seaCreatures ->
            seaCreatures.map {
                it.toSeaCreatureData()
            }
        }
    }

    private fun removeSeaCreature(seaCreature: SeaCreature) {
        seaCreature.stopSwim()
        _seaCreaturesFlow.value -= seaCreature
    }
}