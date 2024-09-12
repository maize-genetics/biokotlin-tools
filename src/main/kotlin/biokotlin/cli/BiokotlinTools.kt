package biokotlin.cli

import biokotlin.util.bufferedReader
import biokotlin.util.setupDebugLogging
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.options.versionOption

/**
 * This class is the main class for the BioKotlin Tools command line interface.
 * It is a subclass of CliktCommand and is used to provide users a BioKotlin command line interface.
 *
 */
class BiokotlinTools : CliktCommand() {

    init {
        setupDebugLogging()

        context {
            helpFormatter = { MordantHelpFormatter(it, showRequiredTag = true, showDefaultValues = true) }
        }

        // get version from version.properties file
        var majorVersion = 0
        var minorVersion = 0
        var patchVersion = 0

        // Try to get the version.properties file from the jar file
        // If that fails, try to get it from the current working directory
        val reader = try {
            BiokotlinTools::class.java.getResourceAsStream("/version.properties").bufferedReader()
        } catch (e: Exception) {
            val path = System.getProperty("user.dir")
            println("Getting version from: ${path}/version.properties")
            bufferedReader("${path}/version.properties")
        }

        reader.readLines().forEach {
            val (key, value) = it.split("=")
            when (key) {
                "majorVersion" -> majorVersion = value.toInt()
                "minorVersion" -> minorVersion = value.toInt()
                "patchVersion" -> patchVersion = value.toInt()
            }
        }
        val version = "$majorVersion.$minorVersion.$patchVersion"
        versionOption(version)
    }

    override fun run() = Unit
}

fun main(args: Array<String>) = BiokotlinTools()
    .subcommands(
        MafToGvcfConverter(),
        ValidateGVCFs(),
        MergeGVCFs(),
        ValidateVCFs(),
        MutateProteins()
    )
    .main(args)