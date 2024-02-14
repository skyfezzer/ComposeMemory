package fr.skyfezzer.composememorygame.util

import fr.skyfezzer.composememorygame.model.Tile

sealed class GameAction {
    data class TileClicked(val clickedTile: Tile) : GameAction()
    data object ResetGame : GameAction()
    data object PauseTimer : GameAction()
}
