package work.racka.reluct.compose.common.date.time.picker.time

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDateTime
import work.racka.reluct.android.compose.components.datetime.time.ClockScreen
import work.racka.reluct.android.compose.components.datetime.time.TimePickerState
import work.racka.reluct.compose.common.date.time.picker.core.*
import work.racka.reluct.compose.common.date.time.picker.util.*
import kotlin.math.*

/* Offset of the clock line and selected circle */
private data class SelectedOffset(
    val lineOffset: Offset = Offset.Zero,
    val selectedOffset: Offset = Offset.Zero,
    val selectedRadius: Float = 0.0f,
)

/**
 * @brief A time picker dialog
 *
 * @param initialTime The time to be shown to the user when the dialog is first shown.
 * Defaults to the current time if this is not set
 * @param colors see [TimePickerColors]
 * @param waitForPositiveButton if true the [onTimeChange] callback will only be called when the
 * positive button is pressed, otherwise it will be called on every input change
 * @param timeRange any time outside this range will be disabled
 * @param is24HourClock uses the 24 hour clock face when true
 * @param onTimeChange callback with a LocalTime object when the user completes their input
 */
@Composable
fun Timepicker(
    modifier: Modifier = Modifier,
    dialogState: DateTimeDialogState = rememberDateTimeDialogState(),
    initialTime: LocalDateTime = currentLocalDateTime(),
    title: String = "SELECT TIME",
    colors: TimePickerColors = TimePickerDefaults.colors(),
    waitForPositiveButton: Boolean = true,
    showExpanded: Boolean = isLargeDevice(),
    timeRange: ClosedRange<LocalDateTime> = LocalDateTimeRange.MIN..LocalDateTimeRange.MAX,
    is24HourClock: Boolean = false,
    onTimeChange: (LocalDateTime) -> Unit = {},
) {
    val timePickerState = remember {
        TimePickerState(
            selectedTime = initialTime.coerceIn(timeRange),
            colors = colors,
            timeRange = timeRange,
            is24Hour = is24HourClock
        )
    }

    DateTimeDialog(
        modifier = modifier,
        isVisible = dialogState.showing,
        onCloseDialog = { dialogState.hide() },
        onPositiveButtonClicked = {
            if (waitForPositiveButton) onTimeChange(timePickerState.selectedTime)
        },
        containerColor = colors.dialogBackgroundColor,
        shape = dialogState.dialogProperties.shape,
        positiveButtonText = dialogState.buttonText.positiveText,
        negativeButtonText = dialogState.buttonText.negativeText
    ) {
        if (showExpanded) {
            TimePickerExpandedImpl(title = title, state = timePickerState)
        } else {
            TimePickerImpl(title = title, state = timePickerState)
        }
    }

    if (!waitForPositiveButton) {
        DisposableEffect(timePickerState.selectedTime) {
            onTimeChange(timePickerState.selectedTime)
            onDispose { }
        }
    }
}

@Composable
internal fun TimePickerExpandedImpl(
    title: String,
    state: TimePickerState,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(start = 24.dp, end = 24.dp)) {
        Box(Modifier.align(Alignment.Start)) {
            TimePickerTitle(modifier = Modifier.height(36.dp), text = title, state = state)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Column(
                Modifier
                    .padding(top = 72.dp, bottom = 50.dp)
                    .width(216.dp)
            ) {
                TimeLayout(state = state)
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalPeriodPicker(state = state)
            }

            /* This isn't an exact match to the material spec as there is a contradiction it.
            Dialogs should be limited to the size of 560 dp but given sizes for extended
            time picker go over this limit */
            Spacer(modifier = Modifier.width(40.dp))
            Crossfade(state.currentScreen) {
                when (it) {
                    ClockScreen.Hour -> if (state.is24Hour) {
                        ExtendedClockHourLayout(state = state)
                    } else {
                        ClockHourLayout(state = state)
                    }
                    ClockScreen.Minute -> ClockMinuteLayout(state = state)
                }
            }
        }
    }
}

@Composable
internal fun TimePickerImpl(
    title: String,
    state: TimePickerState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (title != "") {
            Box(Modifier.align(Alignment.Start)) {
                TimePickerTitle(modifier = Modifier.height(52.dp), text = title, state = state)
            }
        }

        TimeLayout(state = state)

        Spacer(modifier = Modifier.height(if (isSmallDevice()) 24.dp else 36.dp))
        Crossfade(state.currentScreen) {
            when (it) {
                ClockScreen.Hour -> if (state.is24Hour) {
                    ExtendedClockHourLayout(state = state)
                } else {
                    ClockHourLayout(state = state)
                }
                ClockScreen.Minute -> ClockMinuteLayout(state = state)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ExtendedClockHourLayout(state: TimePickerState) {
    fun adjustAnchor(anchor: Int): Int = when (anchor) {
        0 -> 12
        12 -> 0
        else -> anchor
    }

    val isEnabled: (Int) -> Boolean = remember(state.timeRange) {
        { index -> adjustAnchor(index) in state.hourRange() }
    }

    ClockLayout(
        anchorPoints = 12,
        innerAnchorPoints = 12,
        label = { index ->
            /* Swapping 12 and 00 as this is the standard layout */
            when (index) {
                0 -> "12"
                12 -> "00"
                else -> index.toString()
            }
        },
        onAnchorChange = { anchor ->
            /* Swapping 12 and 00 as this is the standard layout */
            state.selectedTime =
                state.selectedTime.withHour(adjustAnchor(anchor)).coerceIn(state.timeRange)
        },
        startAnchor = adjustAnchor(state.selectedTime.hour),
        onLift = { state.currentScreen = ClockScreen.Minute },
        colors = state.colors,
        isAnchorEnabled = isEnabled
    )
}

@Composable
private fun ClockHourLayout(state: TimePickerState) {
    fun adjustedHour(hour: Int): Int {
        return if (state.selectedTime.isAM || hour == 12) hour else hour + 12
    }

    val isEnabled: (Int) -> Boolean = remember(state.timeRange, state.selectedTime) {
        { index -> adjustedHour(index) in state.hourRange() }
    }

    ClockLayout(
        anchorPoints = 12,
        label = { index -> if (index == 0) "12" else index.toString() },
        onAnchorChange = { hours ->
            val adjustedHour = when (hours) {
                12 -> if (state.selectedTime.isAM) 0 else 12
                else -> if (state.selectedTime.isAM) hours else hours + 12
            }
            state.selectedTime = state.selectedTime.withHour(adjustedHour).coerceIn(state.timeRange)
        },
        startAnchor = state.selectedTime.simpleHour % 12,
        onLift = { state.currentScreen = ClockScreen.Minute },
        colors = state.colors,
        isAnchorEnabled = isEnabled
    )
}

@Composable
private fun ClockMinuteLayout(state: TimePickerState) {
    val isEnabled: (Int) -> Boolean =
        remember(state.timeRange, state.selectedTime, state.selectedTime.isAM) {
            { index ->
                index in state.minuteRange(state.selectedTime.isAM, state.selectedTime.hour)
            }
        }
    ClockLayout(
        anchorPoints = 60,
        label = { index -> index.toString().padStart(2, '0') },
        onAnchorChange = { mins -> state.selectedTime = state.selectedTime.withMinute(mins) },
        startAnchor = state.selectedTime.minute,
        isNamedAnchor = { anchor -> anchor % 5 == 0 },
        colors = state.colors,
        isAnchorEnabled = isEnabled
    )
}

@Composable
internal fun TimePickerTitle(text: String, state: TimePickerState, modifier: Modifier = Modifier) {
    Box(modifier) {
        Text(
            text,
            modifier = Modifier.paddingFromBaseline(top = 28.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = state.colors.headerTextColor()
        )
    }
}

@Composable
internal fun ClockLabel(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(20.dp),
) {
    Surface(
        modifier = Modifier
            .width(if (isSmallDevice()) 80.dp else 96.dp)
            .fillMaxHeight(),
        shape = shape,
        color = backgroundColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 50.sp),
                color = textColor
            )
        }
    }
}

@Composable
internal fun TimeLayout(state: TimePickerState, modifier: Modifier = Modifier) {
    val clockHour: String = remember(
        state.is24Hour,
        state.selectedTime,
        state.selectedTime.hour
    ) {
        if (state.is24Hour) {
            state.selectedTime.hour.toString().padStart(2, '0')
        } else {
            state.selectedTime.simpleHour.toString()
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
    ) {
        ClockLabel(
            text = clockHour,
            backgroundColor = state.colors.backgroundColor(state.currentScreen.isHour()).value,
            textColor = state.colors.textColor(state.currentScreen.isHour()).value,
            onClick = { state.currentScreen = ClockScreen.Hour }
        )

        Box(
            Modifier
                .width(24.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = ":",
                style = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 50.sp),
                color = LocalContentColor.current
            )
        }

        ClockLabel(
            text = state.selectedTime.minute.toString().padStart(2, '0'),
            backgroundColor = state.colors.backgroundColor(state.currentScreen.isMinute()).value,
            textColor = state.colors.textColor(state.currentScreen.isMinute()).value,
            onClick = { state.currentScreen = ClockScreen.Minute }

        )

        if (!state.is24Hour) {
            Spacer(modifier = Modifier.width(12.dp))
            VerticalPeriodPicker(state = state)
        }
    }
}

@Composable
private fun VerticalPeriodPicker(
    state: TimePickerState,
    shape: Shape = RoundedCornerShape(20.dp),
) {
    val topPeriodShape = RoundedCornerShape(20.dp).copy(
        bottomStart = CornerSize(0.dp),
        bottomEnd = CornerSize(0.dp)
    )
    val bottomPeriodShape =
        RoundedCornerShape(20.dp).copy(topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp))
    val isAMEnabled = remember(state.timeRange) { state.timeRange.start.hour <= 12 }
    val isPMEnabled = remember(state.timeRange) { state.timeRange.endInclusive.hour >= 0 }

    Column(
        Modifier
            .fillMaxHeight()
            .width(52.dp)
            .border(state.colors.border, shape)
    ) {
        Box(
            modifier = Modifier
                .size(height = 40.dp, width = 52.dp)
                .clip(topPeriodShape)
                .background(state.colors.periodBackgroundColor(state.selectedTime.isAM).value)
                .then(
                    if (isAMEnabled) {
                        Modifier.clickable {
                            state.selectedTime = state.selectedTime
                                .toAM()
                                .coerceIn(state.timeRange)
                        }
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "AM",
                style = MaterialTheme.typography.bodyLarge,
                color = state.colors
                    .textColor(state.selectedTime.isAM).value
                    .copy(alpha = if (isAMEnabled) ContentAlpha.high else ContentAlpha.disabled)
            )
        }

        Spacer(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(state.colors.border.brush)
        )

        Box(
            modifier = Modifier
                .size(height = 40.dp, width = 52.dp)
                .clip(bottomPeriodShape)
                .background(state.colors.periodBackgroundColor(!state.selectedTime.isAM).value)
                .then(
                    if (isPMEnabled) {
                        Modifier.clickable {
                            state.selectedTime = state.selectedTime
                                .toPM()
                                .coerceIn(state.timeRange)
                        }
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "PM",
                style = MaterialTheme.typography.bodyLarge,
                color = state.colors.textColor(!state.selectedTime.isAM).value.copy(
                    alpha = if (isPMEnabled) ContentAlpha.high else ContentAlpha.disabled
                )
            )
        }
    }
}

@Composable
private fun HorizontalPeriodPicker(
    state: TimePickerState,
    shape: Shape = RoundedCornerShape(10.dp),
) {
    val leftPeriodShape = RoundedCornerShape(10.dp).copy(
        bottomEnd = CornerSize(0.dp),
        topEnd = CornerSize(0.dp)
    )
    val rightPeriodShape = RoundedCornerShape(10.dp).copy(
        topStart = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp)
    )
    val isAMEnabled = remember(state.timeRange) { state.timeRange.start.hour <= 12 }
    val isPMEnabled = remember(state.timeRange) { state.timeRange.endInclusive.hour >= 0 }

    Row(
        Modifier
            .fillMaxWidth()
            .height(height = 40.dp)
            .border(state.colors.border, shape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .clip(leftPeriodShape)
                .background(state.colors.periodBackgroundColor(state.selectedTime.isAM).value)
                .then(
                    if (isAMEnabled) {
                        Modifier.clickable {
                            state.selectedTime = state.selectedTime
                                .toAM()
                                .coerceIn(state.timeRange)
                        }
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "AM",
                style = MaterialTheme.typography.bodyLarge,
                color = state.colors
                    .textColor(state.selectedTime.isAM).value
                    .copy(alpha = if (isAMEnabled) ContentAlpha.high else ContentAlpha.disabled)
            )
        }

        Spacer(
            Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(state.colors.border.brush)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(rightPeriodShape)
                .background(state.colors.periodBackgroundColor(!state.selectedTime.isAM).value)
                .then(
                    if (isPMEnabled) {
                        Modifier.clickable {
                            state.selectedTime = state.selectedTime
                                .toPM()
                                .coerceIn(state.timeRange)
                        }
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "PM",
                style = MaterialTheme.typography.bodyLarge,
                color = state.colors.textColor(!state.selectedTime.isAM).value.copy(
                    alpha = if (isPMEnabled) ContentAlpha.high else ContentAlpha.disabled
                )
            )
        }
    }
}

@Composable
private fun ClockLayout(
    anchorPoints: Int,
    label: (Int) -> String,
    startAnchor: Int,
    colors: TimePickerColors,
    isAnchorEnabled: (Int) -> Boolean,
    isNamedAnchor: (Int) -> Boolean = { true },
    innerAnchorPoints: Int = 0,
    onAnchorChange: (Int) -> Unit = {},
    onLift: () -> Unit = {},
) {
    BoxWithConstraints {
        val faceDiameter = min(maxHeight.value, maxWidth.value).coerceAtMost(256f).dp
        val faceDiameterPx = with(LocalDensity.current) { faceDiameter.toPx() }

        val faceRadiusPx = faceDiameterPx / 2f

        val outerRadiusPx = faceRadiusPx * 0.8f
        val innerRadiusPx = remember(outerRadiusPx) { outerRadiusPx * 0.6f }

        val textSizePx = with(LocalDensity.current) { 18.sp.toPx() }
        val innerTextSizePx = remember(textSizePx) { textSizePx * 0.8f }

        val selectedRadius = remember(outerRadiusPx) { outerRadiusPx * 0.2f }
        val selectedInnerDotRadius = remember(selectedRadius) { selectedRadius * 0.2f }
        val innerSelectedRadius = remember(innerRadiusPx) { innerRadiusPx * 0.3f }

        val centerCircleRadius = remember(selectedRadius) { selectedRadius * 0.4f }
        val selectedLineWidth = remember(centerCircleRadius) { centerCircleRadius * 0.5f }

        val center = remember { Offset(faceRadiusPx, faceRadiusPx) }

        val namedAnchor = remember(isNamedAnchor) { mutableStateOf(isNamedAnchor(startAnchor)) }
        val selectedAnchor = remember { mutableStateOf(startAnchor) }

        val anchors = remember(anchorPoints, innerAnchorPoints) {
            val anchors = mutableListOf<SelectedOffset>()
            for (x in 0 until anchorPoints) {
                val angle = (2 * PI / anchorPoints) * (x - 15)
                val selectedOuterOffset = outerRadiusPx.getOffset(angle)
                val lineOuterOffset = (outerRadiusPx - selectedRadius).getOffset(angle)

                anchors.add(
                    SelectedOffset(
                        lineOuterOffset,
                        selectedOuterOffset,
                        selectedRadius
                    )
                )
            }
            for (x in 0 until innerAnchorPoints) {
                val angle = (2 * PI / innerAnchorPoints) * (x - 15)
                val selectedOuterOffset = innerRadiusPx.getOffset(angle)
                val lineOuterOffset = (innerRadiusPx - innerSelectedRadius).getOffset(angle)

                anchors.add(
                    SelectedOffset(
                        lineOuterOffset,
                        selectedOuterOffset,
                        innerSelectedRadius
                    )
                )
            }
            anchors
        }

        val anchoredOffset = remember(anchors, startAnchor) { mutableStateOf(anchors[startAnchor]) }

        val updateAnchor: (Offset) -> Boolean = remember(anchors, isAnchorEnabled) {
            { newOffset ->
                val absDiff = anchors.map {
                    val diff = it.selectedOffset - newOffset + center
                    diff.x.pow(2) + diff.y.pow(2)
                }

                val minAnchor = absDiff.withIndex().minByOrNull { (_, f) -> f }!!.index
                if (isAnchorEnabled(minAnchor)) {
                    if (anchoredOffset.value.selectedOffset != anchors[minAnchor].selectedOffset) {
                        onAnchorChange(minAnchor)

                        anchoredOffset.value = anchors[minAnchor]
                        namedAnchor.value = isNamedAnchor(minAnchor)
                        selectedAnchor.value = minAnchor
                    }
                    true
                } else {
                    false
                }
            }
        }

        val dragSuccess = remember { mutableStateOf(false) }

        val dragObserver: suspend PointerInputScope.() -> Unit = {
            detectDragGestures(
                onDragStart = { dragSuccess.value = true },
                onDragCancel = { dragSuccess.value = false },
                onDragEnd = { if (dragSuccess.value) onLift() },
            ) { change, _ ->
                dragSuccess.value = updateAnchor(change.position)
                if (change.positionChange() != Offset.Zero) change.consume()
            }
        }

        val tapObserver: suspend PointerInputScope.() -> Unit = {
            detectTapGestures(
                onPress = {
                    val anchorsChanged = updateAnchor(it)
                    val success = tryAwaitRelease()

                    if ((success || !dragSuccess.value) && anchorsChanged) {
                        onLift()
                    }
                }
            )
        }

        val inactiveTextColor = colors.textColor(false)
        val clockBackgroundColor = colors.backgroundColor(false).value
        val selectorColor = remember { colors.selectorColor() }
        val selectorTextColor = remember { colors.selectorTextColor() }

        val enabledAlpha = ContentAlpha.high
        val disabledAlpha = ContentAlpha.disabled

        Canvas(
            modifier = Modifier
                .size(faceDiameter)
                .pointerInput(null, dragObserver)
                .pointerInput(null, tapObserver)
        ) {
            drawCircle(clockBackgroundColor, radius = faceRadiusPx, center = center)
            drawCircle(selectorColor, radius = centerCircleRadius, center = center)
            drawLine(
                color = selectorColor,
                start = center,
                end = center + anchoredOffset.value.lineOffset,
                strokeWidth = selectedLineWidth,
                alpha = 0.8f
            )

            drawCircle(
                selectorColor,
                center = center + anchoredOffset.value.selectedOffset,
                radius = anchoredOffset.value.selectedRadius,
                alpha = 0.7f
            )

            if (!namedAnchor.value) {
                drawCircle(
                    Color.White,
                    center = center + anchoredOffset.value.selectedOffset,
                    radius = selectedInnerDotRadius,
                    alpha = 0.8f
                )
            }

            drawIntoCanvas { canvas ->
                fun drawAnchorText(
                    anchor: Int,
                    textSize: Float,
                    radius: Float,
                    angle: Double,
                    alpha: Float = 1f,
                ) {
                    val textOuter = label(anchor)
                    val textColor = if (selectedAnchor.value == anchor) {
                        selectorTextColor
                    } else {
                        inactiveTextColor.value
                    }

                    val contentAlpha = if (isAnchorEnabled(anchor)) enabledAlpha else disabledAlpha

                    drawText(
                        textSize,
                        textOuter,
                        center,
                        angle.toFloat(),
                        canvas,
                        radius,
                        alpha = contentAlpha.coerceAtMost(alpha),
                        color = textColor
                    )
                }

                for (x in 0 until 12) {
                    val angle = (2 * PI / 12) * (x - 15)
                    drawAnchorText(x * anchorPoints / 12, textSizePx, outerRadiusPx, angle)

                    if (innerAnchorPoints > 0) {
                        drawAnchorText(
                            x * innerAnchorPoints / 12 + anchorPoints,
                            innerTextSizePx,
                            innerRadiusPx,
                            angle,
                            alpha = 0.8f
                        )
                    }
                }
            }
        }
    }
}

private fun drawText(
    textSize: Float,
    text: String,
    center: Offset,
    angle: Float,
    canvas: Canvas,
    radius: Float,
    alpha: Float = 1f,
    color: Color = Color.White,
) {
    val outerTextPaint = Paint().apply {
        this.color = color
        this.alpha = alpha
    }

    var r = Rect.Zero
    r = outerTextPaint.getTextBounds(text, 0, text.length, r, textSize)

    canvas.drawTextHelper(
        text = text,
        x = center.x + (radius * cos(angle)),
        y = center.y + (radius * sin(angle)) + (abs(r.height)) / 2,
        paint = outerTextPaint,
        textSize = textSize
    )
}
