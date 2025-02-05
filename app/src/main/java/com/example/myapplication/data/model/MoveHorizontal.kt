package com.example.myapplication.data.model

class MoveHorizontal : MoveBehavior {
    override fun move(seaCreature: SeaCreature, bounds: Pair<Float, Float>): Pair<Float, Float> {
        var (x, y) = seaCreature.position
        val (vx, vy) = seaCreature.velocity

        x += seaCreature.velocity.first

        val newVx = when {
            x < 0 -> vx + seaCreature.turnFactor
            x + seaCreature.size > bounds.first -> vx - seaCreature.turnFactor
            else -> vx
        }

        seaCreature.velocity = Pair(newVx, vy)
        return Pair(x, y)
    }
}