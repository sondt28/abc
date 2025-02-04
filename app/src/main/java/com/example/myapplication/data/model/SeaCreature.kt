package com.example.myapplication.data.model

import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    abstract fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float>

    fun startSwim(scope: CoroutineScope, bounds: Pair<Float, Float>) {
        job = scope.launch {
            while (job?.isActive == true) {
                delay(16)
                position = swimming(bounds)
            }
        }
    }

    fun stopSwim() {
        job?.cancel()
    }

    fun increaseSizeAfterEating() {
        this.size += 5
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
