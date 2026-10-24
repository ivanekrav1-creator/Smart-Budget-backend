plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}
val testcontainersVersion: String by project
dependencies {
    testImplementation(project(":service:smart-budget"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
}
tasks.test {
    useJUnitPlatform()
}
tasks {
    bootJar {
        enabled = false
    }
}
