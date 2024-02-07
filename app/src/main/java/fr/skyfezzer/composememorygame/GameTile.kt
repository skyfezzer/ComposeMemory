package fr.skyfezzer.composememorygame

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun GameTile(
    tile: Tile,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
    ){
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(32.dp))
            .bounceClickable(onClick = { onClick() })
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
                    tile.type,
                    "drawable",
                    LocalContext.current.packageName
                )
                else R.drawable.placeholder
            ),
            contentDescription = null, // Content description can be set as needed
            modifier = Modifier.fillMaxSize()
        )

        Log.i("Tile drawing :","Tile draw $tile")
    }
}

