package com.justnow.compose_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ProgressBarX(100f, 80f)
            }
        }
    }
}

@Composable
fun ProgressBarX(max: Float, current: Float) {
    var playAnimate by remember {
        mutableStateOf(false)
    }
    val percentState =
        animateFloatAsState(
            targetValue = if (playAnimate) (current / max) else 0f,
            animationSpec = tween(durationMillis = 700, delayMillis = 0)
        )
    LaunchedEffect(key1 = true) {
        playAnimate = true
    }
    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(100.dp), onDraw = {
            drawArc(
                Color.Cyan,
                -90f,
                360 * percentState.value,
                false,
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
        })
        Text(fontSize = 30.sp, text = String.format("%.1f", percentState.value), modifier = Modifier.clickable {
            playAnimate = !playAnimate
        })
    }
}

@Composable
fun AnimationBox(modifier: Modifier = Modifier) {
    var boxSize by remember {
        mutableStateOf(300.dp)
    }
    val animateSize by animateDpAsState(
        targetValue = boxSize,
        animationSpec = tween(200, 100, easing = LinearEasing),
        label = "boxSize"
    )
    Box(
        modifier = modifier
            .size(animateSize)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            boxSize += 10.dp
        }) {
            Text(text = "change")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBox(modifier: Modifier = Modifier) {
    var textFiledState by remember {
        mutableStateOf("")
    }
    val snackBarState = remember {
        SnackbarHostState()
    }
    SnackbarHost(hostState = snackBarState)
    val snackBarScope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = textFiledState, onValueChange = {
            textFiledState = it
        })
        Button(onClick = {
            snackBarScope.launch {
                snackBarState.showSnackbar(textFiledState)
            }
        }) {
            Text(text = "click me")
        }
    }
}
