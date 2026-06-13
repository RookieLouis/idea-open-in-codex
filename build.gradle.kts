plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.7.2"
}

group = "com.github.lkjian.ideaopenincodex"
version = "1.1.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "223"
        }
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2026.1.1")
        bundledPlugin("com.intellij.java")
        instrumentationTools()
    }
}
