import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.kyori.indra.git.IndraGitExtension
import org.eclipse.jgit.lib.Repository
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.the
import xyz.jpenilla.gremlin.gradle.ShadowGremlin

/**
 * Relocate a package into the `osakana.libs.` namespace.
 */
fun Task.relocateDependency(pkg: String) {
    ShadowGremlin.relocate(this, pkg, "osakana.libs.$pkg")
}

/**
 * Relocates dependencies which we bundle and relocate on all platforms.
 */
fun Task.standardRelocations() {
    relocateDependency("org.bstats")
    relocateDependency("net.kyori.adventure.serializer.configurate4")
    relocateDependency("net.kyori.moonshine")
    relocateDependency("org.spongepowered.configurate")
    relocateDependency("com.zaxxer.hikari")
    relocateDependency("xyz.jpenilla.gremlin")
}

fun Task.relocateCloud() {
    relocateDependency("org.incendo.cloud")
}

fun ShadowJar.configureShadowJar() {
    standardRelocations()
    dependencies {
        // not needed or provided by platform at runtime
        exclude(dependency("com.google.code.findbugs:jsr305"))
        exclude(dependency("com.google.errorprone:error_prone_annotations"))
        exclude { it.moduleGroup == "com.google.guava" }
        exclude(dependency("com.google.j2objc:j2objc-annotations"))
        exclude(dependency("io.netty:netty-all"))
        exclude(dependency("io.netty:netty-buffer"))
        exclude(dependency("it.unimi.dsi:fastutil"))
        exclude(dependency("org.checkerframework:checker-qual"))
        exclude(dependency("org.slf4j:slf4j-api"))
    }
}

fun Project.lastCommitHash(): String =
    the<IndraGitExtension>().commit()?.name?.substring(0, 8)
        ?: error("Could not determine commit hash")

fun Project.decorateVersion() {
    val versionString = version as String
    version = if (versionString.endsWith("-SNAPSHOT")) {
        "$versionString+${lastCommitHash()}"
    } else {
        versionString
    }
}

fun Project.currentBranch(): String {
    System.getenv("GITHUB_HEAD_REF")?.takeIf { it.isNotEmpty() }
        ?.let { return it }
    System.getenv("GITHUB_REF")?.takeIf { it.isNotEmpty() }
        ?.let { return it.replaceFirst("refs/heads/", "") }

    val indraGit = the<IndraGitExtension>().takeIf { it.isPresent }

    val ref = indraGit?.git()?.repository?.exactRef("HEAD")?.target
        ?: return "detached-head"

    return Repository.shortenRefName(ref.name)
}

val Project.libs: LibrariesForLibs
    get() = the()
