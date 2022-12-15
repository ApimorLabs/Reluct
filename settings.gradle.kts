rootProject.name = "Reluct"
include(
    ":desktop:app",
    ":android:app",
    ":android:benchmark",
    ":android:navigation",
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
    ":common:network",
    ":common:persistence:database",
    ":common:persistence:settings",
    ":common:system-services",
    ":compose-common:charts",
    ":compose-common:components",
    ":compose-common:date-time-picker",
    ":compose-common:pager",
    ":compose-common:theme",
)

pluginManagement {
    includeBuild("build-configs")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
