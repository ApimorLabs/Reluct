package quality

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.configure

object SetDetekt {
    fun Project.configure(taskConfig: (Detekt) -> Unit = {}) {
        plugins.apply("io.gitlab.arturbosch.detekt")

        // val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.configure<DetektExtension> {
            config = files("$rootDir/config/detekt/detekt.yml")
            buildUponDefaultConfig = true
            ignoredBuildTypes = listOf("release")
            source = files(
                DetektExtension.DEFAULT_SRC_DIR_JAVA,
                DetektExtension.DEFAULT_TEST_SRC_DIR_JAVA,
                DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
                DetektExtension.DEFAULT_TEST_SRC_DIR_KOTLIN,
                // Kotlin Multiplatform
                "src/commonMain/kotlin",
                "src/commonTest/kotlin",
                "src/androidMain/kotlin",
                "src/androidTest/kotlin",
                "src/iosMain/kotlin",
                "src/iosTest/kotlin",
                "src/jvmMain/kotlin",
                "src/jvmTest/kotlin",
                "src/desktopMain/kotlin",
                "src/desktopTest/kotlin",
                "src/jsMain/kotlin",
                "src/jsTest/kotlin",
            )
        }

        tasks.withType(Detekt::class.java).configureEach detekt@{
            exclude("**/build/**", "**/generated/**", "**/resources/**")
            basePath = rootProject.projectDir.absolutePath
            autoCorrect = true // Auto corrects common formatting issues
            // Configure reports here
            reports {
                xml.required.set(false)
                txt.required.set(false)
                md.required.set(false)
                sarif.required.set(false)
                html {
                    required.set(true)
                    outputLocation.set(
                        layout.buildDirectory.file("reports/detekt.html")
                    )
                }
            }
            taskConfig(this)
        }

        tasks.withType(DetektCreateBaselineTask::class.java).configureEach detekt@{
            exclude("**/build/**", "**/generated/**", "**/resources/**")
            basePath = rootProject.projectDir.absolutePath
        }

        filterDetektFromCheckTask()

        /*dependencies {
            detektPlugins(libs.detekt_formatting)
            detektPlugins(libs.twitter_compose_rules)
        }*/
    }

    /**
     * By default Detekt runs when the Gradle :check task is triggered
     * It should be disabled for now until everything has been correctly setup (including CI)
     */
    private fun Project.filterDetektFromCheckTask() {
        if (tasks.names.contains("check")) {
            tasks.named("check").configure {
                this.setDependsOn(this.dependsOn.filterNot {
                    it is TaskProvider<*> && it.name == "detekt"
                })
            }
        }
    }
}