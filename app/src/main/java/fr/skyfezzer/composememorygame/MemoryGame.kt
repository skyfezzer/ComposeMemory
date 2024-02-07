package fr.skyfezzer.composememorygame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MemoryGame(
    tilesList: List<Tile>,
    onAction: (GameAction) -> Unit
) {
    // Space between each tiles
    val spaceBetween = 8.dp
    // Number of cards to be displayed and played with
    val cardsAmount = tilesList.size

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0 until cardsAmount / 3) {
            Row(modifier = Modifier.padding(top = spaceBetween)) {
                for (j in 0 until 3) {
                    GameTile(
                        tile = tilesList[i * 3 + j],
                        onClick = {
                            onAction(GameAction.TileClicked(tilesList[i * 3 + j]))
                        }
                    )

                    Spacer(modifier = Modifier.width(spaceBetween))
                }
            }
        }
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
    val viewModel = MemoryGameViewModel()
    MemoryGame(
        tilesList = viewModel.tilesList,
        onAction = viewModel::onAction
    )
}



/*
@SuppressLint("DiscouragedApi")
@Composable
fun TileCard(drawable: String, onTileClick: (Tile) -> Unit, modifier: Modifier = Modifier) {
    val tile by remember { mutableStateOf(Tile(type = drawable)) }
    Surface(
        shape = RoundedCornerShape(32.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .size(100.dp)
            .bounceClickable(onClick = { onTileClick(tile) })
            .background(
                color = if (tile.faceUp) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(32.dp)
            )
            .fillMaxWidth(1f)
            .then(modifier)
    ) {
        Image(
            painter = painterResource(
                id =
                if (tile.faceUp) LocalContext.current.resources.getIdentifier(
                    drawable,
                    "drawable",
                    LocalContext.current.packageName
                )
                else R.drawable.placeholder
            ),
            contentDescription = null, // Content description can be set as needed
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun MemoryGame() {
    // Background Gradient
    val largeRadialGradient = object : ShaderBrush() {
        override fun createShader(size: Size): Shader {
            val biggerDimension = maxOf(size.height, size.width)
            return RadialGradientShader(
                colors = listOf(Color(0xFF842424), Color(0xFF300b0b)),
                center = size.center,
                radius = biggerDimension / 2f,
                colorStops = listOf(0f, 0.95f)
            )
        }
    }
    // Space between each tiles
    val spaceBetween = 8.dp
    // Number of cards to be displayed and played with
    val cardsAmount = 18
    // List of images from Resource Manager
    val imagesList = mutableListOf(
        "bee",
        "bulb",
        "castle",
        "rabbit",
        "rocket",
        "wave",
        "woods",
        "dragon",
        "face",
        "fox",
        "totoro",
        "tree",
        "wizard"
    )
    // Shuffled list that we'll pick from in order to lay out game tiles.
    val shuffledList by remember {
        mutableStateOf((1..cardsAmount / 2).flatMap {
            val randomValue = imagesList.random()
            listOf(randomValue, randomValue)
        }.shuffled())
    }

    // For game mechanic we need to track if a tile has been clicked and stored or not
    var selectedTile: Tile? by remember { mutableStateOf(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(largeRadialGradient),
        contentAlignment = Alignment.Center

    ) {
        Column {
            for (i in 0 until cardsAmount / 3) {
                Row(modifier = Modifier.padding(top = spaceBetween)) {
                    for (j in 0 until 3) {
                        val tileType = shuffledList[i * 3 + j]
                        TileCard(
                            drawable = tileType,
                            onTileClick = { clickedTile ->
                                if (selectedTile == null) {
                                    // First tile clicked
                                    selectedTile = clickedTile
                                    clickedTile.faceUp = true
                                } else {
                                    // Second tile clicked, compare and reset selectedTile
                                    if (selectedTile?.type == clickedTile.type) {
                                        // matching logic here

                                    } else {
                                        // non-matching logic here
                                        // Add a delay to see the non-matching tiles before flipping back down
                                        GlobalScope.launch {
                                            delay(1000)
                                            clickedTile.faceUp = false
                                            selectedTile?.faceUp = false
                                        }
                                    }
                                    selectedTile = null
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(spaceBetween))
                    }
                }
            }
        }
    }
}
*/
