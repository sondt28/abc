package com.example.myapplication.data.model

import android.util.Log
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt

abstract class SeaCreature(
    val id: Long = System.currentTimeMillis(),
    var velocity: Pair<Int, Int> = Pair(0, 0),
    var position: Pair<Float, Float> = Pair(0f, 0f),
    var size: Int,
    val canEatOther: Boolean = false,
    val imageRes: Int,
    var moveBehavior: MoveBehavior
) {
    private var job: Job? = null
//    private var onPositionChanged: ((Pair<Float, Float>) -> Unit) = {}

    abstract fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float>

    fun startSwim(scope: CoroutineScope, bounds: Pair<Float, Float>, seaCreatures: List<SeaCreature>, onPositionChanged: ((Pair<Float, Float>) -> Unit)) {
        job = scope.launch {
            while (job?.isActive == true) {
                delay(16)

                val separationForce = calculateSeparationForce(seaCreatures)
                position = swimming(bounds)
                onPositionChanged(position)
            }
        }
    }


    private fun calculateSeparationForce(boids: List<SeaCreature>): Pair<Float, Float> {
        val SEPARATION_RADIUS = 100f
        val SEPARATION_FORCE = 0.1f
        var forceX = 0f
        var forceY = 0f
        var count = 0

        for (other in boids) {
            if (other == this) continue
            val dx = position.first - other.position.first
            val dy = position.second - other.position.second
            val distance = sqrt(dx * dx + dy * dy)

//            Log.d("SonLN", "Checking ${other.hashCode()} Distance: $distance")

            if (distance < SEPARATION_RADIUS && distance > 0) {
//                Log.d("SonLN", "d")
                forceX += dx / distance
                forceY += dy / distance
                count++
                Log.d("SonLN", "distance $distance $forceX $forceY $count")
            }
        }

        if (count > 0) {
            Log.d("SonLN", "count $count")
            forceX = (forceX / count) * SEPARATION_FORCE
            forceY = (forceY / count) * SEPARATION_FORCE
        }
//        Log.d("SonLN", "distance $forceX $forceY")
        return Pair(forceX, forceY)
    }

    fun stopSwim() {
        job?.cancel()
    }

    fun increaseSizeAfterEating() {
        this.size += 0
    }

    fun turnAround(seaCreature: SeaCreature) {
        this.velocity = Pair(
            if (seaCreature.velocity.first < 0) -velocity.first else velocity.first,
            if (seaCreature.velocity.second < 0) -velocity.second else velocity.second
        )
    }
}

class Shark(
    size: Int = 250,
    velocity: Pair<Int, Int> = Pair(8, 2),
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
    velocity: Pair<Int, Int> = Pair(3, 2),
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
