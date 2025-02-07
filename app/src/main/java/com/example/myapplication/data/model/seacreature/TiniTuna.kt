package com.example.myapplication.data.model.seacreature

import com.example.myapplication.R

class TiniTuna(
    size: Int = 90,
    velocity: Pair<Float, Float> = Pair(3f, 2f),
    imageRes: Int = R.drawable.img_tuna,
    position: Pair<Float, Float>,
    maxSize: Int = 100,
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

        x += velocity.first

        val newVx = when {
            x - 40 < 0 -> vx + turnFactor
            x + 40 + size > bounds.first -> vx - turnFactor
            else -> vx
        }

        velocity = Pair(newVx, vy)
        position = Pair(x, y)

        return position
    }

    override fun getType(): SeaCreatureType {
        return SeaCreatureType.TINI_TUNA
    }
}