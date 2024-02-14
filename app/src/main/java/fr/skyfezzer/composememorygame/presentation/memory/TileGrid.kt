package fr.skyfezzer.composememorygame.presentation.memory

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.skyfezzer.composememorygame.util.GameAction

@Composable
fun TileGrid(
    spaceBetween: Dp,
    state: GameState,
    onAction: (GameAction) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        content = {
            items(state.tilesList.size) { i ->
                Tile(
                    tile = state.tilesList[i],
                    modifier = Modifier.padding(spaceBetween),
                    onClick = {
                        onAction(GameAction.TileClicked(state.tilesList[i]))
                    })
            }
        }
    )
    /*for (i in 0 until state.tilesList.size / 3) {
        Row(modifier = Modifier.padding(top = spaceBetween)) {
            var debug = ""
            for (j in 0 until 3) {
                debug += (" \t| " + state.tilesList[i * 3 + j])
                Tile(
                    modifier = Modifier.weight(1f),
                    tile = state.tilesList[i * 3 + j],
                    onClick = {
                        onAction(GameAction.TileClicked(state.tilesList[i * 3 + j]))
                    }
                )

                Spacer(modifier = Modifier.width(spaceBetween))
            }
            Log.i("Tile drawing :", debug)
        }
    }*/
}