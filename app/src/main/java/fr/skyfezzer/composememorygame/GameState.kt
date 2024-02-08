package fr.skyfezzer.composememorygame

data class GameState(
    var tilesList: List<Tile>,
    val timer: Float = 0f,
    val amountOfTries: Int = 0,
    var rememberedTile: Tile? = null,
    val firstClick: Boolean = true
)