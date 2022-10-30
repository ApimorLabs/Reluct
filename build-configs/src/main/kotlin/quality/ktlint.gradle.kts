package quality

import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    id("org.jlleitschuh.gradle.ktlint")
}

configure<KtlintExtension> {
    verbose.set(true)
    outputToConsole.set(true)

    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

dependencies {
    //ktlintRuleset()
}
