package com.example.myapplication.data.model

import com.example.myapplication.R

abstract class SeaCreature(
    val id: Long = System.currentTimeMillis(),
    var size: Int,
    var velocity: Pair<Int, Int> = Pair(0, 0),
    val canEatOther: Boolean = false,
    val image: Int,
    var position: Pair<Float, Float> = Pair(0f, 0f),
    var moveBehavior: MoveBehavior
) {
    abstract fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float>

    fun increaseSizeAfterEating() {
        this.size += 20
    }
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
