package com.example.myapplication.data.model.seacreature

import com.example.myapplication.R

class TiniTuna(
    size: Int = 90,
    velocity: Pair<Float, Float> = Pair(3f, 2f),
    imageRes: Int = R.drawable.img_tuna,
    position: Pair<Float, Float>,
    maxSize: Int = 100,
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

        x += velocity.first

        val newVx = when {
            x - MARGIN_BOUND < 0 -> vx + TURN_FACTOR
            x + MARGIN_BOUND + size > bounds.first -> vx - TURN_FACTOR
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