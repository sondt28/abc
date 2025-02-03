package com.example.myapplication.data.model

import android.util.Log
import com.example.myapplication.R

abstract class SeaCreature(
    val id: Long = System.currentTimeMillis(),
    val size: Int,
    var velocity: Pair<Int, Int> = Pair(0, 0),
    val canEatOther: Boolean = false,
    val image: Int,
    var position: Pair<Float, Float> = Pair(0f, 0f)
) {
    abstract fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float>
}

class Shark(
    size: Int = 50,
    velocity: Pair<Int, Int> = Pair(2, 2),
    canEatOther: Boolean = true,
    image: Int = R.drawable.nemo,
    position: Pair<Float, Float>
) : SeaCreature(size = size, velocity = velocity, canEatOther = canEatOther, image = image, position = position) {

    override fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float> {
        val (newX, newY) = position

        if (newX < 0 || newX >= bounds.first) {
            velocity = Pair(-velocity.first, velocity.second)
        }
        if (newY < 0 || newY >= bounds.second) {
            velocity = Pair(velocity.first, -velocity.second)
        }

        position = Pair(newX + velocity.first, newY + velocity.second)

        return position
    }

    fun copy(
        size: Int = this.size,
        velocity: Pair<Int, Int> = this.velocity,
        canEatOther: Boolean = this.canEatOther,
        image: Int = this.image,
        position: Pair<Float, Float> = this.position
    ): Shark {
        return Shark(
            size = size,
            velocity = velocity,
            canEatOther = canEatOther,
            image = image,
            position = position
        )
    }
}
