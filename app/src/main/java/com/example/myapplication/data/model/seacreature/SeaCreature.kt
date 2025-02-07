package com.example.myapplication.data.model.seacreature

import android.util.Log
import com.example.myapplication.util.Const.DELAY_TIME_MS
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
    val maxSize: Int,
    val imageRes: Int,
    open val limitSpeed: Pair<Float, Float> = Pair(1f, 10f)
) {
    private var job: Job? = null
    protected val turnFactor = 0.5f
    protected val marginBound = 90f
    var onEaten: ((SeaCreature) -> Unit) = {}

    abstract fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float>
    abstract fun getType(): SeaCreatureType

    fun startSwim(
        scope: CoroutineScope,
        bounds: Pair<Float, Float>,
        onPositionChanged: ((Pair<Float, Float>) -> Unit),
    ) {
        job = scope.launch {
            while (job?.isActive == true) {
                delay(DELAY_TIME_MS)
                setLimitSpeed(limitSpeed.first, limitSpeed.second)
                position = swimming(bounds)
                onPositionChanged(position)
            }
        }
    }

    private fun handleEncounter(other: SeaCreature) {
        if (this.size < other.size && other.canEatOther) {
            this.turnAround(other)
        }
    }

    private fun handleEating(other: SeaCreature) {
        if (this.canEat(other)) {
            onEaten(other)
            increaseSize()
        }
    }

    private fun increaseSize() {
        val temp = this.size + 10
        if (temp <= maxSize) {
            this.size = temp
        } else {
            this.size = maxSize
        }
    }

    private fun setLimitSpeed(minSpeed: Float, maxSpeed: Float) {
        val (vx, vy) = this.velocity
        val speed = sqrt(vx * vx + vy * vy)

        if (speed > maxSpeed) {
            this.velocity = Pair((vx / speed) * maxSpeed, (vy / speed) * maxSpeed)
        } else if (speed < minSpeed) {
            this.velocity = Pair((vx / speed) * minSpeed, (vy / speed) * minSpeed)
        }
    }

    private fun turnAround(other: SeaCreature) {
        val directionX = this.position.first - other.position.first
        val directionY = this.position.second - other.position.second

        this.velocity = Pair(
            if (directionX > 0) Math.abs(this.velocity.first) else -Math.abs(this.velocity.first),
            if (directionY > 0) Math.abs(this.velocity.second) else -Math.abs(this.velocity.second)
        )
    }

    private fun canEat(other: SeaCreature): Boolean {
        return this.canEatOther && this.size > other.size
    }

    fun detectCollisions(seaCreatures: List<SeaCreature>) {
        for (other in seaCreatures) {
            if (other == this) continue

            val distance = calculateDistance(other)
            val deadRange = (this.size + other.size) / 2
            val warningRange = (this.size + other.size)

            when {
                distance < deadRange -> handleEating(other)
                distance < warningRange -> handleEncounter(other)
            }
        }
    }

    private fun calculateDistance(b: SeaCreature): Double {
        return sqrt(
            (this.position.first - b.position.first).toDouble().pow(2) +
                    (this.position.second - b.position.second).toDouble().pow(2)
        )
    }

    fun stopSwim() {
        job?.cancel()
        job = null
    }
}




