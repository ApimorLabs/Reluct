package work.racka.reluct.compose.common.components.topBar

/*
** Experimenting!!
@Composable
fun ReluctContentTopBar(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    shape: Shape = RectangleShape,
    colors: TopAppBarColors = TopAppBarDefaults
        .mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
            navigationIconContentColor = LocalContentColor.current,
            titleContentColor = LocalContentColor.current,
            actionIconContentColor = LocalContentColor.current
        ),
    minShrinkHeight: Dp = 64.dp,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    content: @Composable BoxScope.() -> Unit,
) {

    // TODO(b/182393826): Check if there is a better place to set the offsetLimit.
    // Set a scroll offset limit to hide the entire app bar area when scrolling.
    val offsetLimit = with(LocalDensity.current) { -minShrinkHeight.toPx() }
    SideEffect {
        if (scrollBehavior?.state?.offsetLimit != offsetLimit) {
            scrollBehavior?.state?.offsetLimit = offsetLimit
        }
    }

    // Obtain the container color from the TopAppBarColors.
    // This may potentially animate or interpolate a transition between the container-color and the
    // container's scrolled-color according to the app bar's scroll state.
    val scrollFraction = scrollBehavior?.scrollFraction ?: 0f
    val appBarContainerColor by colors.containerColor(scrollFraction)

    val animatedColor by animateColorAsState(
        targetValue = appBarContainerColor,
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    )


    val height = LocalDensity.current.run {
        minShrinkHeight.toPx() + (scrollBehavior?.state?.offset ?: 0f)
    }

    Surface(
        modifier = modifier
            .height(height.dp)
            //.offset(y = scrollBehavior?.offset?.dp ?: 0f.dp)
            .fillMaxWidth()
            .background(
                color = animatedColor,
                shape = shape
            )
    ) {
//        val animatedCardSize by animateDpAsState(
//            targetValue = height.dp,
//            animationSpec = tween(300, easing = LinearOutSlowInEasing)
//        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = contentAlignment,
            content = content
        )
    }
}*/
