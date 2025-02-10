package com.example.myapplication.data.model.seacreature

import com.example.myapplication.R

class Turtle(
    size: Int = 160,
    velocity: Pair<Float, Float> = Pair(2f, 1f),
    imageRes: Int = R.drawable.img_turtle,
    position: Pair<Float, Float>,
    canEatOther: Boolean = true,
    maxSize: Int = 200,
    override val limitSpeed: Pair<Float, Float> = Pair(1f, 3f)
) : SeaCreature(
    size = size,
    velocity = velocity,
    imageRes = imageRes,
    position = position,
    canEatOther = canEatOther,
    maxSize = maxSize
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
        return SeaCreatureType.TURTLE
    }
}