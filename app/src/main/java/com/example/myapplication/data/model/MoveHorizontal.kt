package com.example.myapplication.data.model


class MoveHorizontal : MoveBehavior {
    override fun move(seaCreature: SeaCreature, bounds: Pair<Float, Float>): Pair<Float, Float> {
        var (newX, newY) = seaCreature.position
        newX += seaCreature.velocity.first

        if (newX < 0 || newX + seaCreature.size >= bounds.first) {
            seaCreature.velocity = Pair(-seaCreature.velocity.first, seaCreature.velocity.second)
        }

        return Pair(newX, newY)
    }
}