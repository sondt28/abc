package com.example.myapplication.data.model.seacreature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
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
    var onEaten: ((SeaCreature) -> Unit) = {}
    var onPositionChanged: (SeaCreature) -> Unit = {}

    abstract fun move(bounds: Pair<Float, Float>): Pair<Float, Float>
    abstract fun getType(): SeaCreatureType

    fun startSwim(
        scope: CoroutineScope,
        bounds: Pair<Float, Float>
    ) {
        job = scope.launch {
            while (job?.isActive == true) {
                delay(DELAY_TIME_MS)
                setLimitSpeed(limitSpeed.first, limitSpeed.second)
                position = move(bounds)
                onPositionChanged(this@SeaCreature)
            }
        }
    }

    fun eating(other: SeaCreature) {
        onEaten(other)
        increaseSize()
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

    fun escapeFrom(other: SeaCreature) {
        val directionX = this.position.first - other.position.first
        val directionY = this.position.second - other.position.second

        this.velocity = Pair(
            if (directionX > 0) this.velocity.first else -this.velocity.first,
            if (directionY > 0) this.velocity.second else -this.velocity.second
        )
    }

    fun stopSwim() {
        job?.cancel()
        job = null
    }

    fun toSeaCreatureData(): SeaCreatureData {
        return SeaCreatureData(
            id = id,
            size = size,
            image = imageRes,
            position = position,
            velocity = velocity
        )
    }

    companion object {
        const val TURN_FACTOR = 0.5f
        const val MARGIN_BOUND = 90f
        const val DELAY_TIME_MS: Long = 16
    }
}




