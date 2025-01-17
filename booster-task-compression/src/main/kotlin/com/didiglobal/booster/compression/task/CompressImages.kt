package com.didiglobal.booster.compression.task

import com.android.build.api.variant.Variant
import com.didiglobal.booster.aapt2.Aapt2Container
import com.didiglobal.booster.aapt2.resourceName
import com.didiglobal.booster.command.Command
import com.didiglobal.booster.command.CommandInstaller
import com.didiglobal.booster.compression.CompressionOptions
import com.didiglobal.booster.compression.CompressionResult
import com.didiglobal.booster.compression.CompressionResults
import com.didiglobal.booster.compression.CompressionTool
import com.didiglobal.booster.kotlinx.Wildcard
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import java.io.File

/**
 * Represents task for image compression
 *
 * @author johnsonlee
 */
abstract class CompressImages<T : CompressionOptions> : DefaultTask() {

    @get:Internal
    lateinit var variant: Variant

    @get:Internal
    lateinit var tool: CompressionTool

    @get:Internal
    lateinit var results: CompressionResults

    @get:Internal
    lateinit var options: T

    @get:Internal
    lateinit var images: () -> Collection<File>

    /**
     * The resource path filter
     */
    @get:Internal
    var filter: ResourceNameFilter = MATCH_ALL_RESOURCES

    @get:Internal
    val compressor: File
        get() = project.tasks.withType(CommandInstaller::class.java).find {
            it.command == tool.command
        }!!.location

    @get:Input
    val command: Command
        get() = tool.command

    @get:Input
    val variantName: String
        get() = variant.name

    protected open fun includes(arg: Pair<File, Aapt2Container.Metadata>): Boolean {
        val (input, metadata) = arg
        return if (filter(metadata.resourceName)) true else false.also {
            ignore(metadata.resourceName, input, File(metadata.sourcePath))
        }
    }

    protected open fun includes(input: File): Boolean {
        return if (filter(input.resourceName)) true else false.also {
            ignore(input.resourceName, input, input)
        }
    }

    protected fun ignore(resName: String, dest: File, src: File) {
        val s0 = dest.length()
        results.add(CompressionResult(dest, s0, s0, src))
        logger.info("${tool.command.name}: exclude $resName $dest => $src")
    }

}

internal typealias ResourceNameFilter = (String) -> Boolean

internal val MATCH_ALL_RESOURCES: ResourceNameFilter = { true }

internal fun excludes(ignores: Set<Wildcard>): ResourceNameFilter = { res ->
    ignores.none {
        it.matches(res)
    }
}

data class ActionData(val input: File, val output: File, val cmdline: List<String>)

data class Aapt2ActionData(val input: File, val metadata: Aapt2Container.Metadata, val output: File, val cmdline: List<String>, val aapt2: List<String>)
