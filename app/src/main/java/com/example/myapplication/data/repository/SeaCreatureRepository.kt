package com.example.myapplication.data.repository

import com.example.myapplication.data.model.SeaCreature
import com.example.myapplication.data.model.SeaCreatureData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.pow

class SeaCreatureRepository(private val bounds: Pair<Float, Float>) {
    private val _seaCreatureListFlow = MutableStateFlow<List<SeaCreature>>(listOf())

    val noti: MutableSharedFlow<Boolean> = MutableSharedFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val swimmingJobs = mutableMapOf<Long, Job>()

    fun addSeaCreature(seaCreature: SeaCreature) {
        val currentList = _seaCreatureListFlow.value.toMutableList()
        currentList.add(seaCreature)
        _seaCreatureListFlow.value = currentList

        swimmingJobs[seaCreature.id]?.cancel()

        val job = coroutineScope.launch {
            while (isActive) {
                delay(16)
                seaCreature.swimming(bounds)
                checkCollisions()
                _seaCreatureListFlow.value = _seaCreatureListFlow.value.toMutableList()
                noti.emit(true)
            }
        }

        swimmingJobs[seaCreature.id] = job
    }

    fun getSeaCreaturesData(): List<SeaCreatureData> {
        return _seaCreatureListFlow.value.map {
            SeaCreatureData(
                id = it.id,
                size = it.size,
                image = it.image,
                position = it.position,
                velocity = it.velocity
            )
        }
    }

    private fun checkCollisions() {
        val creatures = _seaCreatureListFlow.value.toMutableList()
        for (i in creatures.indices) {
            for (j in i + 1 until creatures.size) {
                val creatureA = creatures[i]
                val creatureB = creatures[j]

                if (isColliding(creatureA, creatureB)) {
                    handleCollision(creatureA, creatureB)
                }
            }
        }
    }

    private fun isColliding(a: SeaCreature, b: SeaCreature): Boolean {
        val distance = Math.sqrt(
            ((a.position.first - b.position.first).toDouble().pow(2) + (a.position.second - b.position.second).toDouble().pow(2))
        )
        val collisionDistance = (a.size + b.size) / 2.0
        return distance < collisionDistance
    }

    private fun handleCollision(a: SeaCreature, b: SeaCreature) {
//        if (a.canEatOther && !b.canEatOther) {
//            removeSeaCreature(b)
//        } else if (b.canEatOther && !a.canEatOther) {
//            removeSeaCreature(a)
//        } else {
//            a.velocity = Pair(-a.velocity.first, -a.velocity.second)
//            b.velocity = Pair(-b.velocity.first, -b.velocity.second)
//        }
        if (a.canEatOther) {
            removeSeaCreature(b)
            a.increaseSizeAfterEating()
        }
    }

    fun removeSeaCreature(seaCreature: SeaCreature) {
        swimmingJobs[seaCreature.id]?.cancel()
        swimmingJobs.remove(seaCreature.id)

        val currentList = _seaCreatureListFlow.value.toMutableList()
        currentList.remove(seaCreature)
        _seaCreatureListFlow.value = currentList
    }
}