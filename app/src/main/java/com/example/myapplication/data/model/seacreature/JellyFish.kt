package com.example.myapplication.data.model.seacreature

import com.example.myapplication.R

class JellyFish(
    size: Int = 150,
    velocity: Pair<Float, Float> = Pair(3f, 3f),
    imageRes: Int = R.drawable.img_jellyfish,
    position: Pair<Float, Float>,
    maxSize: Int = 160,
    override val limitSpeed: Pair<Float, Float> = Pair(1f, 5f)
) : SeaCreature(
    size = size,
    velocity = velocity,
    imageRes = imageRes,
    position = position,
    maxSize = maxSize
) {
    override fun move(bounds: Pair<Float, Float>): Pair<Float, Float> {
        var (x, y) = position
        val (vx, vy) = velocity

        y += velocity.second

        val newVy = when {
            y - MARGIN_BOUND < 0 -> vy + TURN_FACTOR
            y + MARGIN_BOUND + size > bounds.first -> vy - TURN_FACTOR
            else -> vy
        }

        velocity = Pair(vx, newVy)

        position = Pair(x, y)
        return position
    }

    override fun getType(): SeaCreatureType {
        return SeaCreatureType.JELLYFISH
    }
}
