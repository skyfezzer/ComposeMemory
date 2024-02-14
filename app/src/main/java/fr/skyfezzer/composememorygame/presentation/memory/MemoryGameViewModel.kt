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

// TODO : Use androidx.compose.runtime.snapshots.SnapshotStateList in order to handle tiles list.

// TODO : Find a way to dissociate timer recomposition from TileGrid recomposition
// Reason : Bad handling of Tiles state. Should use SnapshotStateList
// https://stackoverflow.com/questions/74699081
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
    private fun onTileClicked(clickedTile: Tile) {
        Log.i(
            "onTileClicked",
            (if (state.firstClick) "first" else "second") + " click on $clickedTile"
        )
        // Starting chrono in case it isn't already.
        state = state.copy(playing = true)

        // constraint verification
        if (clickedTile.faceUp) {
            // Clicked tile is already face up, skipping.
            return
        }
        // constraint verification
        if (state.firstClick && state.rememberedTile != null) {
            return
        }

        // Flip up actual clicked tile before any algorithm.
        this.updateFaceInState(
            tile = clickedTile,
            isUp = true
        )

        // If it's our first selection of the pair
        if (state.firstClick) {
            // We update state with the tile to remember, will check pair at second click.
            state = state.copy(
                firstClick = false,
                rememberedTile = clickedTile
            )
            // If not, we need to check bunch of stuff
        } else {
            // Constraint check
            if (state.rememberedTile == null) {
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

                // else, if they are the same type
            }else{
                // Right pair found, update the state and let them stay face up.
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
            // Shuffle the initial resource list
            .shuffled()
            // Removes n first elements, in order to get a list of size WANTED_SIZE / 2
            .drop(tileResNames.size - (size / 2))
            // ForEach resource String, create a Tile Object and push it to the final list.
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
            }
            // Shuffle for pair randomization (otherwise pairs next to each-other).
            .shuffled()
    }


}

fun <T> List<T>.updateElement(predicate: (T) -> Boolean, transform: (T) -> T): List<T> {
    return map { if (predicate(it)) transform(it) else it }
}


