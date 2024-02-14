package fr.skyfezzer.composememorygame.model

import kotlin.random.Random

data class Tile(val type: String, val faceUp: Boolean = false, val id: Int = Random.nextInt())
