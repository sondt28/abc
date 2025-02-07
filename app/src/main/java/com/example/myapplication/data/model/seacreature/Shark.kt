package com.example.myapplication.data.model.seacreature

import com.example.myapplication.R

class Shark(
    size: Int = 140,
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
    override fun swimming(bounds: Pair<Float, Float>): Pair<Float, Float> {
        var (x, y) = position
        val (vx, vy) = velocity

        val newVx = when {
            x - marginBound < 0 -> vx + turnFactor
            x + marginBound + size > bounds.first -> vx - turnFactor
            else -> vx
        }

        val newVy = when {
            y - marginBound < 0 -> vy + turnFactor
            y + marginBound + size > bounds.second -> vy - turnFactor
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