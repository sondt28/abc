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
    var onEaten: ((SeaCreature) -> Unit) = {}
    val turnFactor = 0.5f

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
        val (bigger, smaller) = if (a.size > b.size) a to b else b to a

        if (smaller.size < bigger.size) {
            smaller.turnAround(bigger)
        }
    }

    private fun handleCollisions(a: SeaCreature, b: SeaCreature) {
        val (bigger, smaller) = if (a.size > b.size) a to b else b to a
        if (bigger.canEatOther) {
            bigger.increaseSizeAfterEating()
            onEaten(smaller)
        }
    }

    fun stopSwim() {
        job?.cancel()
    }

    fun increaseSizeAfterEating() {
        this.size += 50
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
