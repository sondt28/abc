package com.example.myapplication.util

import com.example.myapplication.data.model.seacreature.SeaCreature
import com.example.myapplication.data.model.seacreature.SeaCreatureType
import com.example.myapplication.data.model.seacreature.JellyFish
import com.example.myapplication.data.model.seacreature.Shark
import com.example.myapplication.data.model.seacreature.TiniTuna
import com.example.myapplication.data.model.seacreature.Turtle

interface SeaCreatureFactory {
    fun create(map: Map<String, Any>) : SeaCreature
}

class SeaCreatureSelectionFactory : SeaCreatureFactory {
    override fun create(map: Map<String, Any>): SeaCreature {
        val type = map["type"] as SeaCreatureType
        val position = map["position"] as Pair<Float, Float>

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

class SeaCreatureRandomFactory : SeaCreatureFactory {
    override fun create(map: Map<String, Any>): SeaCreature {
        val position = map["position"] as Pair<Float, Float>
        val randomType = SeaCreatureType.entries.toTypedArray().random()

        return when (randomType) {
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
