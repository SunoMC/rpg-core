plugins {
    `java-library`
    `maven-publish`
}

group = "net.sunomc.rpg"
version = "1.0.1"
description = "rpg-core"

val targetJavaVersion = 21
val targetMcVersion = "1.21.5"
val targetLombokVersion = "1.18.38"

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")

}

dependencies {
    compileOnly("org.projectlombok:lombok:$targetLombokVersion")
    annotationProcessor("org.projectlombok:lombok:$targetLombokVersion")

    testCompileOnly("org.projectlombok:lombok:$targetLombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$targetLombokVersion")

    compileOnly("io.papermc.paper:paper-api:$targetMcVersion-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])

        groupId = group.toString()
        artifactId = description
        version = version
    }

}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
