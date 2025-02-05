package com.example.myapplication.data.model

import android.util.Log
import kotlinx.coroutines.runBlocking

class MoveHorizontal : MoveBehavior {
    override fun move(seaCreature: SeaCreature, bounds: Pair<Float, Float>): Pair<Float, Float> {
        var (newX, newY) = seaCreature.position

        newX += seaCreature.velocity.first

        if (newX < 0 || newX + seaCreature.size > bounds.first) {
            seaCreature.velocity = Pair(-seaCreature.velocity.first, seaCreature.velocity.second)
            seaCreature.originalVelocity = Pair(-seaCreature.originalVelocity.first, seaCreature.originalVelocity.second)
        }

        return Pair(newX, newY)
    }
}