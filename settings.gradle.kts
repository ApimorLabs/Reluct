rootProject.name = "Reluct"
include(
    ":desktop:app",
    ":android:app",
    ":android:compose:components",
    ":android:compose:charts",
    ":common:model",
    ":common:integration",
    ":common:persistence:database",
    ":common:persistence:settings",
)
