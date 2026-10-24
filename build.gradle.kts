allprojects {
    repositories {
        mavenCentral()
    }
}
plugins {
    java
    id("com.diffplug.spotless") apply false
}
subprojects {
    plugins.apply("com.diffplug.spotless")
    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            target("**/*.java")
            targetExclude("build/generated/**/*.java")
            googleJavaFormat()
            removeUnusedImports()
            trimTrailingWhitespace()
            forbidWildcardImports()
            endWithNewline()
            formatAnnotations()
        }
    }
}
