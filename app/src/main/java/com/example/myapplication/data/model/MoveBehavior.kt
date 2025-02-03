package com.example.myapplication.data.model

interface MoveBehavior {
    fun move(seaCreature: SeaCreature, bounds: Pair<Float, Float>) : Pair<Float, Float>
}