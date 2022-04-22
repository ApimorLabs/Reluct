rootProject.name = "Reluct"
include(
    ":desktop:app",
    ":android:app",
    ":android:compose:components",
    ":android:compose:charts",
    ":android:compose:navigation",
    ":android:compose:theme",
    ":android:screens",
    ":common:compose:destinations",
    ":common:features:tasks",
    ":common:features:goals",
    ":common:model",
    ":common:integration",
    ":common:persistence:database",
    ":common:persistence:settings",
)
