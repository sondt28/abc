package com.example.myapplication.data.model


class MoveZicZac : MoveBehavior {

    override fun move(seaCreature: SeaCreature, bounds: Pair<Float, Float>): Pair<Float, Float> {
        var (newX, newY) = seaCreature.position

        newX += seaCreature.velocity.first
        newY += seaCreature.velocity.second

        if (newX < 0 || newX + seaCreature.size >= bounds.first) {
            seaCreature.velocity = Pair(-seaCreature.velocity.first, seaCreature.velocity.second)
        }
        if (newY < 0 || newY + seaCreature.size >= bounds.second) {
            seaCreature.velocity = Pair(seaCreature.velocity.first, -seaCreature.velocity.second)
        }

        return Pair(newX, newY)
    }
}