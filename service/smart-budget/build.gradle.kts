plugins {
	java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-liquibase")
	implementation("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation(project(":libs:open-api"))
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-kafka")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-mail")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
