package fr.skyfezzer.composememorygame

import kotlin.random.Random

data class Tile(var type: String, var faceUp: Boolean = false, var id : Int = Random.nextInt())
