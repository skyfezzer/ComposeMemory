package fr.skyfezzer.composememorygame

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MemoryGameViewModel : ViewModel() {

    private var tilesAmount = 6
    var state by mutableStateOf(GameState(generateNewTileList(tilesAmount)))
        private set


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
    private fun onTileClicked(clickedTile: Tile) {
        Log.i("onTileClicked",(if (state.firstClick) "first" else "second")+" click on $clickedTile")

        if(clickedTile.faceUp){
            Log.i("onTileClicked","tile already has face up")
            return
        }
        if(state.firstClick && state.rememberedTile != null){
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

                MainScope().launch {
                    delay(1000)
                    // TODO: Rework this variable mess
                    updateFaceInState(
                        tile = clickedTile,
                        isUp = false
                    )
                    updateFaceInState(
                        tile = state.rememberedTile!!,
                        isUp = false
                    )
                    state = state.copy(rememberedTile = null)
                }


            }else{
                this.updateFaceInState(
                    tile = clickedTile,
                    isUp = true
                )
                state = state.copy(rememberedTile = null)
            }
            state = state.copy(firstClick = true)
        }
    }

    private fun resetGame() {
        state = GameState(generateNewTileList(tilesAmount))
    }

    private fun updateFaceInState(tile: Tile, isUp: Boolean) {
        tile.faceUp = isUp
        Log.i("updateFaceInState", "before : $tile")
        var tempList = state.tilesList

        state = state.copy(
            tilesList = tempList.updateElement({ it.id == tile.id }) {
                tile
            }
        )
        Log.i("updateFaceInState", "after : $tile")
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


}

fun <T> List<T>.updateElement(predicate: (T) -> Boolean, transform: (T) -> T): List<T> {
    return map { if (predicate(it)) transform(it) else it }
}


