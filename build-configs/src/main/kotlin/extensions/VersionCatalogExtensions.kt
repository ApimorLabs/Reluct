package extensions

import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

internal val VersionCatalog.ktlintPlugin: Provider<PluginDependency>
    get() = getPlugin("ktlint.gradle.plugin")


private fun VersionCatalog.getLibrary(library: String) = findLibrary(library).get()

private fun VersionCatalog.getBundle(bundle: String) = findBundle(bundle).get()

private fun VersionCatalog.getPlugin(plugin: String) = findPlugin(plugin).get()