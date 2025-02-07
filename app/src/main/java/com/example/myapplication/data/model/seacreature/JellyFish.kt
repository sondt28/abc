package com.example.myapplication.data.model.seacreature

import com.example.myapplication.R

class JellyFish(
    size: Int = 150,
    velocity: Pair<Float, Float> = Pair(3f, 3f),
    imageRes: Int = R.drawable.img_jellyfish,
    position: Pair<Float, Float>,
    maxSize: Int = 170
) : SeaCreature(
    size = size,
    velocity = velocity,
    imageRes = imageRes,
    position = position,
    maxSize = maxSize
) {
    override fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float> {
        var (x, y) = position
        val (vx, vy) = velocity

        y += velocity.second

        val newVy = when {
            y - marginBound < 0 -> vy + turnFactor
            y + marginBound + size > bounds.first -> vy - turnFactor
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
