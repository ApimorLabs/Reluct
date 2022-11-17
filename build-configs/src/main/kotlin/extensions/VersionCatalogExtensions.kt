package extensions

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

internal val VersionCatalog.ktlintPlugin: Provider<PluginDependency>
    get() = getPlugin("ktlint.gradle.plugin")

internal val VersionCatalog.detektGradle: Provider<MinimalExternalModuleDependency>
    get() = getLibrary("detekt.gradle.plugin")

internal val VersionCatalog.androidGradle: Provider<MinimalExternalModuleDependency>
    get() = getLibrary("android.gradle.plugin")

internal val VersionCatalog.detekt_formatting: Provider<MinimalExternalModuleDependency>
    get() = getLibrary("detekt.formatting")

internal val VersionCatalog.twitter_compose_rules: Provider<MinimalExternalModuleDependency>
    get() = getLibrary("detekt.rule.twitter.compose")

private fun VersionCatalog.getLibrary(library: String) = findLibrary(library).get()

private fun VersionCatalog.getBundle(bundle: String) = findBundle(bundle).get()

private fun VersionCatalog.getPlugin(plugin: String) = findPlugin(plugin).get()