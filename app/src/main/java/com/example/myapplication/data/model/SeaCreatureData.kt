package com.example.myapplication.data.model

data class SeaCreatureData(
    val id: Long,
    val size: Int,
    val image: Int,
    val position: Pair<Float, Float>,
    val velocity: Pair<Float, Float>
)
