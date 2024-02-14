package fr.skyfezzer.composememorygame.presentation.memory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.skyfezzer.composememorygame.util.GameAction

@Composable
fun MemoryGame(
    state: GameState,
    onAction: (GameAction) -> Unit
) {
    // Space between each tiles
    val spaceBetween = 8.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(1f)
    ) {
        Text("Temps écoulé : ${"%.2f".format(0f)}s")
        TileGrid(spaceBetween, state, onAction)
        Row(
            modifier = Modifier.padding(top = spaceBetween * 2)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { onAction(GameAction.ResetGame) }
            ) {
                Text("Reset")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = { onAction(GameAction.PauseTimer) }) {
                Text("Pause")
            }
        }
    }
}


@Preview
@Composable
fun PreviewMemoryGame() {
    val viewModel = viewModel<MemoryGameViewModel>()
    MemoryGame(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}