plugins {
    `java-library`
}

allprojects {
    pluginManager.withPlugin("java-library") {
        group = "org.example"
        version = "1.0-SNAPSHOT"

        dependencies {
            testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
            testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://maven.iais.fraunhofer.de/artifactory/eis-ids-public/")
        }
        maven {
            name = "edc-github"
            url = uri("https://maven.pkg.github.com/eclipse-dataspaceconnector/DataSpaceConnector");
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
