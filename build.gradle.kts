@file:OptIn(ExperimentalDistributionDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl

plugins {
  kotlin("multiplatform") version "2.2.20"
}

group = "nl.astraeus"
version = "0.1.0"

repositories {
  mavenCentral()
}

kotlin {
  js {
    browser {
      commonWebpackConfig {
        outputFileName = "fractal.js"
        sourceMaps = true
        devtool = "inline-source-map"
      }

      binaries.executable()

      distribution {
        outputDirectory.set(File("$projectDir/web/"))
      }
    }
  }

  sourceSets {
    val commonMain by getting
    val jsMain by getting
  }
}
