package fr.skyfezzer.composememorygame.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.skyfezzer.composememorygame.presentation.memory.MemoryGame
import fr.skyfezzer.composememorygame.presentation.memory.MemoryGameViewModel

import fr.skyfezzer.composememorygame.ui.theme.ComposeMemoryGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMemoryGameTheme {
                val viewModel = viewModel<MemoryGameViewModel>()
                MemoryGame(
                    state = viewModel.state,
                    onAction = viewModel::onAction
                )
            }
        }
    }
}
