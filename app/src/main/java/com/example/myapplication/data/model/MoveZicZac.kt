package com.example.myapplication.data.model

class MoveZicZac : MoveBehavior {

    override fun move(seaCreature: SeaCreature, bounds: Pair<Float, Float>): Pair<Float, Float> {
        var (x, y) = seaCreature.position
        val (vx, vy) = seaCreature.velocity



        val newVx = when {
            x < 0 -> vx + seaCreature.turnFactor
            x + seaCreature.size > bounds.first -> vx - seaCreature.turnFactor
            else -> vx
        }
        val newVy = when {
            y < 0 -> vy + seaCreature.turnFactor
            y + seaCreature.size > bounds.second -> vy - seaCreature.turnFactor
            else -> vy
        }

        seaCreature.velocity = Pair(newVx, newVy)

        x += seaCreature.velocity.first
        y += seaCreature.velocity.second

        return Pair(x, y)
    }
}