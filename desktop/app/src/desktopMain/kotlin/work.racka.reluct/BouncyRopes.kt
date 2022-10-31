package work.racka.reluct

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

private const val POINT_RADIUS = 40f

@Composable
fun SpringStringDemo() {
    Box(
        Modifier.fillMaxSize()
    ) {
        var firstPointOffset by remember { mutableStateOf(Offset(50f, 100f)) }
        var secondPointOffset by remember { mutableStateOf(Offset(250f, 150f)) }
        val controlPoint by remember {
            derivedStateOf {
                val centerPoint = (firstPointOffset + secondPointOffset) / 2f
                val xDistanceBetweenThePoints = abs(firstPointOffset.x - secondPointOffset.x)
                val centerPointWithDownOffset =
                    centerPoint.plus(Offset(0f, xDistanceBetweenThePoints))
                centerPointWithDownOffset
            }
        }
        var dampingRatio by remember { mutableStateOf(0.1f) }
        var stiffness by remember { mutableStateOf(30f) }
        val animatedControlPoint by animateOffsetAsState(
            targetValue = controlPoint,
            animationSpec = spring(
                visibilityThreshold = Offset(0.1f, 0.1f),
                dampingRatio = dampingRatio,
                stiffness = stiffness,
            ),
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        when {
                            change.previousPosition touches firstPointOffset -> {
                                firstPointOffset += dragAmount
                            }

                            change.previousPosition touches secondPointOffset -> {
                                secondPointOffset += dragAmount
                            }
                        }
                    }
                }
        ) {
            drawCircle(Color.Black, POINT_RADIUS, firstPointOffset)
            drawCircle(Color.Black, POINT_RADIUS, secondPointOffset)
            drawPath(
                path = Path().apply {
                    moveTo(firstPointOffset.x, firstPointOffset.y)
                    quadraticBezierTo(
                        x1 = animatedControlPoint.x,
                        y1 = animatedControlPoint.y,
                        x2 = secondPointOffset.x,
                        y2 = secondPointOffset.y,
                    )
                },
                color = Color.Black,
                style = Stroke(width = 5f),
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text("dampingRatio:$dampingRatio")
                Slider(dampingRatio, { dampingRatio = it }, valueRange = 0.1f..0.5f)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text("stiffness:$stiffness")
                Slider(stiffness, { stiffness = it }, valueRange = 20f..1000f)
            }
        }
    }
}

private infix fun Offset.touches(other: Offset): Boolean {
    return (this distanceFrom other) <= (POINT_RADIUS)
}

private infix fun Offset.distanceFrom(other: Offset): Float {
    return sqrt(
        (other.x - x).pow(2) + (other.y - y).pow(2)
    )
}
