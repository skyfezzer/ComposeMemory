package fr.skyfezzer.composememorygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel

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
