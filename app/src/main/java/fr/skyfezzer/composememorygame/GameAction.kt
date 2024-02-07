package fr.skyfezzer.composememorygame

sealed class GameAction {
    data class TileClicked(val tile: Tile): GameAction()
    data object ResetGame: GameAction()
    data object PauseTimer: GameAction()
}
