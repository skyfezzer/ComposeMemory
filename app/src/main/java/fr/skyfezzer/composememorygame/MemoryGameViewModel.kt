package fr.skyfezzer.composememorygame

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MemoryGameViewModel : ViewModel() {

    private var tilesAmount = 12

    var tilesList by mutableStateOf(generateNewTileList(tilesAmount))

    private var selectedTile by mutableStateOf<Tile?>(null)

    fun onAction(action: GameAction) {
        when(action){
            is GameAction.ResetGame -> resetGame()
            is GameAction.TileClicked-> onTileClicked(action.tile)
            is GameAction.PauseTimer -> pauseTimer()
        }
    }

    private fun pauseTimer() {
        TODO("Not yet implemented")
    }

    // Function to handle tile clicked
    private fun onTileClicked(tile: Tile) {
        if (selectedTile == null) {
            // First tile clicked
            selectedTile = tile
            setFaceUp(tile,true)
        } else {
            // Second tile clicked, compare and reset selectedTile
            if (selectedTile!!.type != tile.type) {
                // Stocking precedent selectedTile temporarily.
                var tempTile = selectedTile
                // Replacing it with the actual clicked tile, in order to flip it up.
                selectedTile = tile
                setFaceUp(selectedTile!!,true)

                // Flipping them back after a couple of seconds, otherwise it would flip back down instantly
                // without revealing itself
                GlobalScope.launch {
                    // TODO: Delay + fix second click
                    delay(1000)
                    // FORCE SETTING SELECTEDTILE BACK
                    // IN ORDER TO AVOID NULLPOINTER
                    selectedTile = tempTile
                    setFaceUp(selectedTile!!,false)
                    selectedTile = tile
                    setFaceUp(selectedTile!!,false)
                    tempTile = null
                    selectedTile = null
                }
            }else{
                selectedTile = tile
                setFaceUp(selectedTile!!,true)
            }
            selectedTile = null
        }
    }

    private fun resetGame() {
        tilesList = generateNewTileList(tilesAmount)
    }

    private fun setFaceUp(tile: Tile, up: Boolean) {
        tilesList = tilesList.updateElement({it.id == tile.id}) {
            it.copy(faceUp = up)
        }
    }

    // Generate a list of Tile objects of size "size"
    private fun generateNewTileList(size: Int): List<Tile> {
        val tileResNames = listOf(
            "bee", "bulb", "castle", "rabbit", "rocket", "wave",
            "woods", "dragon", "face", "fox", "totoro", "tree", "wizard"
        ).shuffled()

        return ((1..(size / 2)).flatMap {
            val type = tileResNames.drop(it).first()
            listOf(Tile(id = it, type = type), Tile(id = it+size,type = type))
        }).shuffled()
    }

    private fun <T> List<T>.updateElement(predicate: (T) -> Boolean, transform: (T) -> T): List<T> {
        return map { if (predicate(it)) transform(it) else it }
    }
}


