package com.example.myapplication.data.model.seacreature

import com.example.myapplication.R

class Shark(
    size: Int = 150,
    velocity: Pair<Float, Float> = Pair(8f, 2f),
    override val canEatOther: Boolean = true,
    imageRes: Int = R.drawable.img_shark,
    position: Pair<Float, Float>,
    maxSize: Int = 210,
) : SeaCreature(
    velocity = velocity,
    position = position,
    size = size,
    maxSize = maxSize,
    imageRes = imageRes
) {
    override fun move(bounds: Pair<Float, Float>): Pair<Float, Float> {
        var (x, y) = position
        val (vx, vy) = velocity

        val newVx = when {
            x - MARGIN_BOUND < 0 -> vx + TURN_FACTOR
            x + MARGIN_BOUND + size > bounds.first -> vx - TURN_FACTOR
            else -> vx
        }

        val newVy = when {
            y - MARGIN_BOUND < 0 -> vy + TURN_FACTOR
            y + MARGIN_BOUND + size > bounds.second -> vy - TURN_FACTOR
            else -> vy
        }

        velocity = Pair(newVx, newVy)

        x += velocity.first
        y += velocity.second

        position = Pair(x, y)

        return position
    }

    override fun getType(): SeaCreatureType {
        return SeaCreatureType.SHARK
    }
}