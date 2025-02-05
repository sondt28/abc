package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.model.SeaCreature
import com.example.myapplication.data.model.SeaCreatureData
import com.example.myapplication.util.Rectangle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.pow

class SeaCreatureRepository(private val bounds: Pair<Float, Float>) {
    private val _seaCreatureListFlow = MutableStateFlow<List<SeaCreature>>(listOf())
    val notifyChangePosition: MutableSharedFlow<Boolean> = MutableSharedFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val swimmingJobs = mutableMapOf<Long, Job>()
//    private val quadTree = QuadTree(Rectangle(0f, 0f, bounds.first, bounds.second))

    fun addSeaCreature(seaCreature: SeaCreature) {
        val currentList = _seaCreatureListFlow.value.toMutableList()
        currentList.add(seaCreature)
        _seaCreatureListFlow.value = currentList

        swimmingJobs[seaCreature.id]?.cancel()
//        quadTree.insert(seaCreature)

//        seaCreature.startSwim(coroutineScope, bounds)

//        val job = coroutineScope.launch {
//            while (isActive) {
//                delay(16)
//                seaCreature.swimming(bounds)
//                checkCollisions()
//                _seaCreatureListFlow.value = _seaCreatureListFlow.value.toMutableList()
//                notifyChangePosition.emit(true)
//            }
//        }

        seaCreature.startSwim(coroutineScope, bounds, _seaCreatureListFlow.value) {
            coroutineScope.launch {
                notifyChangePosition.emit(true)
            }
        }
//        swimmingJobs[seaCreature.id] = job
    }

    private fun checkCollisions(seaCreature: SeaCreature) {
        val range = Rectangle(
            seaCreature.position.first,
            seaCreature.position.second,
            seaCreature.size.toFloat(),
            seaCreature.size.toFloat()
        )
        val foundCreatures = mutableListOf<SeaCreature>()

//        quadTree.query(range, foundCreatures)

        for (otherCreature in foundCreatures) {
            if (otherCreature != seaCreature && isCollision(seaCreature, otherCreature)) {
                handleCollision(seaCreature, otherCreature)
            }
        }
    }

    fun isCollision(creature1: SeaCreature, creature2: SeaCreature): Boolean {
        return creature1.position.first < creature2.position.first + creature2.size &&
                creature1.position.first + creature1.size > creature2.position.first &&
                creature1.position.second < creature2.position.second + creature2.size &&
                creature1.position.second + creature1.size > creature2.position.second
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

    private fun checkCollisions() {
        val creatures = _seaCreatureListFlow.value.toMutableList()
        for (i in creatures.indices) {
            for (j in i + 1 until creatures.size) {
                val creatureA = creatures[i]
                val creatureB = creatures[j]

                isColliding(creatureA, creatureB)
            }
        }
    }

    private fun isWithinRadius(a: SeaCreature, b: SeaCreature): Boolean {
        val distance = hypot(
            (a.position.first - b.position.first).toDouble(),
            (a.position.second - b.position.second).toDouble()
        ).toFloat()
        return distance <= 50f
    }

    private fun isColliding(a: SeaCreature, b: SeaCreature) {
        val distance = Math.sqrt(
            ((a.position.first - b.position.first).toDouble()
                .pow(2) + (a.position.second - b.position.second).toDouble().pow(2))
        )

        val eatDistance = (a.size + b.size) / 2.0
        val dangerDistance = (a.size + b.size)

         when {
            distance < eatDistance -> handleCollision(a, b)
            distance < dangerDistance -> handleAvoidance(a, b)
            else -> {}
        }
    }

    private fun handleAvoidance(a: SeaCreature, b: SeaCreature) {
        val (bigger, smaller) = if (a.size > b.size) a to b else b to a

        if (smaller.size < bigger.size) {
            smaller.turnAround(bigger)
        }
    }

    private fun handleCollision(a: SeaCreature, b: SeaCreature) {
        val (bigger, smaller) = if (a.size > b.size) a to b else b to a

        if (bigger.canEatOther) {
            bigger.increaseSizeAfterEating()
            removeSeaCreature(smaller)
        }
    }

    private fun removeSeaCreature(seaCreature: SeaCreature) {
        swimmingJobs[seaCreature.id]?.cancel()
        swimmingJobs.remove(seaCreature.id)

        val currentList = _seaCreatureListFlow.value.toMutableList()
        currentList.remove(seaCreature)
        _seaCreatureListFlow.value = currentList
    }

    fun removeAll() {
        coroutineScope.cancel()
        swimmingJobs.clear()
        _seaCreatureListFlow.value = listOf()
    }
}