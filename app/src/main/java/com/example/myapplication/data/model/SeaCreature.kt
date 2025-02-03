package com.example.myapplication.data.model

import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.pow

abstract class SeaCreature(
    val id: Long = System.currentTimeMillis(),
    var size: Int,
    var velocity: Pair<Int, Int> = Pair(0, 0),
    val canEatOther: Boolean = false,
    val image: Int,
    var position: Pair<Float, Float> = Pair(0f, 0f),
    var moveBehavior: MoveBehavior
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())


    abstract fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float>

//    private fun startSwimming(bounds: Pair<Float, Float>) {
//        coroutineScope.launch {
//            while (isActive) {
//                delay(16)
//                position = swimming(bounds)
//            }
//        }
//    }

    fun isColliding(other: SeaCreature): Boolean {
        val distance = Math.sqrt(
            ((position.first - other.position.first).toDouble().pow(2) +
                    (position.second - other.position.second).toDouble().pow(2))
        )
        val collisionDistance = (size + other.size) / 2.0
        return distance < collisionDistance
    }

    fun increaseSizeAfterEating(sizeToAdd: Int) {
        this.size += sizeToAdd
    }

//    fun stopSwimming() {
//        coroutineScope.cancel()
//    }
}

class Shark(
    size: Int = 250,
    velocity: Pair<Int, Int> = Pair(8, 2),
    canEatOther: Boolean = true,
    image: Int = R.drawable.shark,
    position: Pair<Float, Float>,
    moveBehavior: MoveBehavior = MoveZicZac()
) : SeaCreature(
    size = size,
    velocity = velocity,
    canEatOther = canEatOther,
    image = image,
    position = position,
    moveBehavior = moveBehavior
) {
    override fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float> {
        position = moveBehavior.move(this, bounds)
        return position
    }
}

class TiniTuna(
    size: Int = 80,
    velocity: Pair<Int, Int> = Pair(3, 2),
    canEatOther: Boolean = false,
    image: Int = R.drawable.tuna,
    position: Pair<Float, Float>,
    moveBehavior: MoveBehavior = MoveHorizontal()
) : SeaCreature(
    size = size,
    velocity = velocity,
    canEatOther = canEatOther,
    image = image,
    position = position,
    moveBehavior = moveBehavior
) {

    override fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float> {
        position = moveBehavior.move(this, bounds)
        return position
    }
}
