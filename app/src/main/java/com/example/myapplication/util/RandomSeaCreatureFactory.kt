package com.example.myapplication.util

import com.example.myapplication.data.model.seacreature.SeaCreature
import com.example.myapplication.data.model.seacreature.SeaCreatureType
import com.example.myapplication.data.model.seacreature.JellyFish
import com.example.myapplication.data.model.seacreature.Shark
import com.example.myapplication.data.model.seacreature.TiniTuna
import com.example.myapplication.data.model.seacreature.Turtle

object RandomSeaCreatureFactory {
    fun create(position: Pair<Float, Float>): SeaCreature {
        val type = SeaCreatureType.values().random()

        return when (type) {
            SeaCreatureType.TINI_TUNA -> {
                return TiniTuna(position = position)
            }

            SeaCreatureType.SHARK -> {
                return Shark(position = position)
            }

            SeaCreatureType.TURTLE -> {
                return Turtle(position = position)
            }

            SeaCreatureType.JELLYFISH -> {
                return JellyFish(position = position)
            }

            else -> {
                throw IllegalArgumentException("Invalid SeaCreatureType")
            }
        }
    }
}