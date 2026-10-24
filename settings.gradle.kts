rootProject.name = "smart-budget"
include("integration-tests",
        "libs:open-api",
        "service:smart-budget",
        "mock-producer"
    )
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        val springBootVersion: String by settings
        val dependencyManagementVersion: String by settings
        val openapiGeneratorVersion: String by settings
        val spotlessVersion: String by settings
        id("com.diffplug.spotless") version spotlessVersion
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version dependencyManagementVersion
        id("org.openapi.generator") version openapiGeneratorVersion
    }
}
