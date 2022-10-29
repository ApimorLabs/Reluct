rootProject.name = "Reluct"
include(
    ":desktop:app",
    ":android:app",
    ":android:benchmark",
    ":android:compose:components",
    ":android:compose:charts",
    ":android:compose:navigation",
    ":android:compose:theme",
    ":android:screens",
    ":android:widgets",
    ":common:app-usage-stats",
    ":common:authentication",
    ":common:billing",
    ":common:core-navigation",
    ":common:domain",
    ":common:di-integration",
    ":common:features:dashboard",
    ":common:features:goals",
    ":common:features:onboarding",
    ":common:features:screen-time",
    ":common:features:settings",
    ":common:features:tasks",
    ":common:model",
    ":common:mvvm-core",
    ":common:persistence:database",
    ":common:persistence:settings",
    ":common:system-services",
)

pluginManagement {
    includeBuild("build-configs")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
