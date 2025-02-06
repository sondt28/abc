package com.example.myapplication.data.model.seacreature

import com.example.myapplication.R

class Turtle(
    size: Int = 180,
    velocity: Pair<Float, Float> = Pair(2f, 1f),
    imageRes: Int = R.drawable.img_turtle,
    position: Pair<Float, Float>
) : SeaCreature(
    size = size,
    velocity = velocity,
    imageRes = imageRes,
    position = position
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
}