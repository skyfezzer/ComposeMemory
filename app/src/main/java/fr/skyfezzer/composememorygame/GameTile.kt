package fr.skyfezzer.composememorygame

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@SuppressLint("DiscouragedApi")
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
            .bounceClickable(onAnimationFinished = { onClick() })
            .background(
                color = if (tile.faceUp) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(32.dp)
            )
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
    }
}

fun Modifier.bounceClickable(
    minScale: Float = .1f,
    onAnimationFinished: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) minScale else 1f,
        label = "",

    ) {
        if (isPressed) {
            isPressed = false
            onAnimationFinished?.invoke()
            Log.i("animation","finished")
        }
    }

    this
        .graphicsLayer {
            scaleX = scale
            //scaleY = scale
        }
        .clickable {
            isPressed = true
            onClick?.invoke()
        }
}
