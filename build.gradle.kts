import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import net.kyori.indra.IndraPlugin

plugins {
	id("java")
	id("net.kyori.indra") version "2.1.1"
	id("io.papermc.paperweight.userdev") version "1.3.7"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.glaremasters"
version = "1.0-SNAPSHOT"

apply {
	plugin<ShadowPlugin>()
	plugin<IndraPlugin>()
}

repositories {
	mavenCentral()
	maven("https://papermc.io/repo/repository/maven-public/")
	maven("https://oss.sonatype.org/content/groups/public/")
	maven("https://repo.aikar.co/content/groups/aikar/")
	maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
	paperDevBundle("1.19-R0.1-SNAPSHOT")

	implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
	implementation("io.github.bananapuncher714:nbteditor:7.18.3")
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
	build {
		dependsOn(reobfJar)
	}

	compileJava {
		options.compilerArgs = listOf("-parameters")
		options.encoding = Charsets.UTF_8.name()
		options.release.set(17)
	}

	indra {
		mitLicense()

		github("darbyjack", "rocketeer")
	}

	shadowJar {
		fun relocates(vararg dependencies: String) {
			dependencies.forEach {
				val split = it.split(".")
				val name = split.last()
				relocate(it, "me.glaremasters.rocketeer.libs.$name")
			}
		}

		relocates(
			"co.aikar.commands",
			"co.aikar.locales",
			"io.github.bananapuncher714"
		)

		minimize()

		archiveClassifier.set(null as String?)
	}

	processResources {
		expand("version" to rootProject.version)
		filteringCharset = Charsets.UTF_8.name()
	}
}
