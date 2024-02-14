package fr.skyfezzer.composememorygame.presentation.memory

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.skyfezzer.composememorygame.model.Tile
import fr.skyfezzer.composememorygame.util.GameAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MemoryGameViewModel : ViewModel() {

    private val tilesAmount = 12

    private val initialList = generateNewTileList(tilesAmount)
    var state by mutableStateOf(GameState(initialList))
        private set

    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.ResetGame -> resetGame()
            is GameAction.TileClicked -> onTileClicked(action.clickedTile)
            is GameAction.PauseTimer -> pauseTimer()
        }
    }

    private fun pauseTimer() {


    }

    // Function to handle tile clicked
    /* TODO: Rework tile storage in memory, as a click triggers whole app recomposition.
       I should use LiveData
    */
    private fun onTileClicked(clickedTile: Tile) {
        Log.i(
            "onTileClicked",
            (if (state.firstClick) "first" else "second") + " click on $clickedTile"
        )
        state = state.copy(playing = true)
        if (clickedTile.faceUp) {
            // Clicked tile is already face up, skipping.
            return
        }
        if (state.firstClick && state.rememberedTile != null) {
            return
        }
        this.updateFaceInState(
            tile = clickedTile,
            isUp = true
        )
        if (state.firstClick) {
            // First tile clicked
            state = state.copy(
                firstClick = false,
                rememberedTile = clickedTile
            )

        } else {
            // Second tile clicked, compare and reset selectedTile
            if(state.rememberedTile == null){
                Log.e("onTileClicked","Second Click captured, rememberedTile shouldn't be null but was.")
                return
            }

            // If our precedent and current tiles aren't the same type
            if (state.rememberedTile!!.type != clickedTile.type) {
                // We display the card face up in order to let know player what card it is,
                // Before flipping back down.
                this.updateFaceInState(
                    tile = clickedTile,
                    isUp = true
                )

                // Flipping them back after a couple of seconds, otherwise it would flip back down instantly
                // without revealing itself

                viewModelScope.launch {
                    delay(1000)
                    updateFaceInState(
                        tile = clickedTile,
                        isUp = false
                    )
                    updateFaceInState(
                        tile = state.rememberedTile!!,
                        isUp = false
                    )
                    state =
                        state.copy(rememberedTile = null, amountOfTries = state.amountOfTries + 1)
                }


            }else{
                this.updateFaceInState(
                    tile = clickedTile,
                    isUp = true
                )
                state = state.copy(rememberedTile = null, amountOfTries = state.amountOfTries + 1)
            }
            state = state.copy(firstClick = true)
        }
    }

    private fun resetGame() {
        state = GameState(generateNewTileList(tilesAmount))
    }

    private fun updateFaceInState(tile: Tile, isUp: Boolean) {
        state = state.copy(
            tilesList = state.tilesList.updateElement({ it.id == tile.id }) {
                tile.copy(faceUp = isUp)
            }
        )
    }

    // Generate a list of Tile objects of size "size"
    private fun generateNewTileList(size: Int): List<Tile> {
        val tileResNames = listOf(
            "bee", "bulb", "castle", "rabbit", "rocket", "wave",
            "woods", "dragon", "face", "fox", "totoro", "tree", "wizard"
        ).shuffled()

        return tileResNames
            .shuffled()
            .drop(tileResNames.size - (size / 2))
            .flatMap {
                listOf(
                    Tile(
                        type = it,
                        id = it.hashCode()
                    ),
                    Tile(
                        type = it,
                        id = it.hashCode() + 1
                    )
                )
            }.shuffled()
    }


}

fun <T> List<T>.updateElement(predicate: (T) -> Boolean, transform: (T) -> T): List<T> {
    return map { if (predicate(it)) transform(it) else it }
}


