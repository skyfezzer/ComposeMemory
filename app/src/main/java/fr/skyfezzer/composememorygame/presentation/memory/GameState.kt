package fr.skyfezzer.composememorygame.presentation.memory

import fr.skyfezzer.composememorygame.model.Tile

data class GameState(
    val tilesList: List<Tile>,
    val amountOfTries: Int = 0,
    val rememberedTile: Tile? = null,
    val firstClick: Boolean = true,
    val playing: Boolean = false
)