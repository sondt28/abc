package com.example.myapplication.data.model

import com.example.myapplication.R
import com.example.myapplication.util.Const.DANGER_DISTANCE_FACTOR
import com.example.myapplication.util.Const.DELAY_TIME_MS
import com.example.myapplication.util.Const.EAT_DISTANCE_FACTOR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

abstract class SeaCreature(
    val id: Long = System.currentTimeMillis(),
    var velocity: Pair<Float, Float> = Pair(0f, 0f),
    var position: Pair<Float, Float> = Pair(0f, 0f),
    var size: Int,
    val canEatOther: Boolean = false,
    val imageRes: Int,
    var moveBehavior: MoveBehavior
) {
    private var job: Job? = null
    var originalVelocity = this.velocity
    var onEaten: ((SeaCreature) -> Unit) = {}

    abstract fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float>

    fun startSwim(
        scope: CoroutineScope,
        bounds: Pair<Float, Float>,
        seaCreatures: List<SeaCreature>,
        onPositionChanged: ((Pair<Float, Float>) -> Unit),
    ) {
        job = scope.launch {
            while (job?.isActive == true) {
                delay(DELAY_TIME_MS)
                val newPosition = swimming(bounds)
                val collisions = detectCollisions(seaCreatures)
                handleCollisions(collisions)
                position = newPosition
                onPositionChanged(position)
            }
        }
    }

    private fun detectCollisions(seaCreatures: List<SeaCreature>): List<Pair<SeaCreature, SeaCreature>> {
        val collisions = mutableListOf<Pair<SeaCreature, SeaCreature>>()
        for (other in seaCreatures) {
            if (other.id == this.id) continue
            val distance = calculateDistance(this, other)
            val eatDistance = (this.size + other.size) / EAT_DISTANCE_FACTOR
            val dangerDistance = (this.size + other.size) * DANGER_DISTANCE_FACTOR

            when {
                distance < eatDistance -> collisions.add(Pair(this, other))
                distance < dangerDistance -> handleAvoidance(this, other)
            }
        }
        return collisions
    }

    private fun calculateDistance(a: SeaCreature, b: SeaCreature): Double {
        return sqrt(
            (a.position.first - b.position.first).toDouble().pow(2) +
                    (a.position.second - b.position.second).toDouble().pow(2)
        )
    }

    private fun handleAvoidance(a: SeaCreature, b: SeaCreature) {
        val (bigger, smaller) = if (a.size > b.size) a to b else b to a

        if (smaller.size < bigger.size) {
            smaller.turnAround(bigger)
        }
    }

    private fun handleCollisions(collisions: List<Pair<SeaCreature, SeaCreature>>) {
        for ((a, b) in collisions) {
            val (bigger, smaller) = if (a.size > b.size) a to b else b to a
            if (bigger.canEatOther) {
                onEaten(smaller)
            }
        }
    }

    fun stopSwim() {
        job?.cancel()
    }

    fun increaseSizeAfterEating() {
        this.size += 0
    }

    fun turnAround(seaCreature: SeaCreature) {
        val directionX = this.position.first - seaCreature.position.first
        val directionY = this.position.second - seaCreature.position.second

        this.velocity = Pair(
            if (directionX > 0) Math.abs(this.velocity.first) else -Math.abs(this.velocity.first),
            if (directionY > 0) Math.abs(this.velocity.second) else -Math.abs(this.velocity.second)
        )
    }
}

class Shark(
    size: Int = 250,
    velocity: Pair<Float, Float> = Pair(8f, 2f),
    canEatOther: Boolean = true,
    imageRes: Int = R.drawable.img_shark,
    position: Pair<Float, Float>,
    moveBehavior: MoveBehavior = MoveZicZac()
) : SeaCreature(
    size = size,
    velocity = velocity,
    canEatOther = canEatOther,
    imageRes = imageRes,
    position = position,
    moveBehavior = moveBehavior
) {
    override fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float> {
        position = moveBehavior.move(this, bounds)
        return position
    }
}

class TiniTuna(
    size: Int = 100,
    velocity: Pair<Float, Float> = Pair(3f, 2f),
    canEatOther: Boolean = false,
    imageRes: Int = R.drawable.img_tuna,
    position: Pair<Float, Float>,
    moveBehavior: MoveBehavior = MoveHorizontal()
) : SeaCreature(
    size = size,
    velocity = velocity,
    canEatOther = canEatOther,
    imageRes = imageRes,
    position = position,
    moveBehavior = moveBehavior
) {

    override fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float> {
        position = moveBehavior.move(this, bounds)
        return position
    }
}
