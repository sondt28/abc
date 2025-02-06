package com.example.myapplication.data.model.seacreature

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
    var velocity: Pair<Float, Float>,
    var position: Pair<Float, Float>,
    var size: Int,
    open val canEatOther: Boolean = false,
    val imageRes: Int,
) {
    private var job: Job? = null
    val turnFactor = 0.5f
    val marginBound = 40f
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
                detectCollisions(seaCreatures)
                limitSpeed(minSpeed = 2f, maxSpeed = 10f)
                position = swimming(bounds)
                onPositionChanged(position)
            }
        }
    }

    private fun detectCollisions(seaCreatures: List<SeaCreature>) {
        for (other in seaCreatures) {
            if (other.id == this.id) continue
            val distance = calculateDistance(this, other)
            val eatDistance = (this.size + other.size) / EAT_DISTANCE_FACTOR
            val dangerDistance = (this.size + other.size) * DANGER_DISTANCE_FACTOR

            when {
                distance < eatDistance -> handleCollisions(this, other)
                distance < dangerDistance -> handleAvoidance(this, other)
            }
        }
    }

    private fun calculateDistance(a: SeaCreature, b: SeaCreature): Double {
        return sqrt(
            (a.position.first - b.position.first).toDouble().pow(2) +
                    (a.position.second - b.position.second).toDouble().pow(2)
        )
    }

    private fun handleAvoidance(a: SeaCreature, b: SeaCreature) {
        if (a.size < b.size && a.canEatOther) {
            a.turnAround(b)
        }
    }

    private fun handleCollisions(a: SeaCreature, b: SeaCreature) {
        val (bigger, smaller) = if (a.size > b.size) a to b else b to a
        if (bigger.canEatOther) {
            onEaten(smaller)
            bigger.increaseSizeAfterEating()
        }
    }

    fun stopSwim() {
        job?.cancel()
    }

    fun increaseSizeAfterEating() {
        this.size++
    }

    private fun limitSpeed(minSpeed: Float, maxSpeed: Float) {
        val (vx, vy) = this.velocity
        val speed = sqrt(vx * vx + vy * vy)

        if (speed > maxSpeed) {
            this.velocity = Pair((vx / speed) * maxSpeed, (vy / speed) * maxSpeed)
        } else if (speed < minSpeed) {
            this.velocity = Pair((vx / speed) * minSpeed, (vy / speed) * minSpeed)
        }
    }

    private fun turnAround(seaCreature: SeaCreature) {
        val directionX = this.position.first - seaCreature.position.first
        val directionY = this.position.second - seaCreature.position.second

        this.velocity = Pair(
            if (directionX > 0) Math.abs(this.velocity.first) else -Math.abs(this.velocity.first),
            if (directionY > 0) Math.abs(this.velocity.second) else -Math.abs(this.velocity.second)
        )
    }
}




