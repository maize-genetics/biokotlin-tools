package biokotlin.cli

import biokotlin.util.ValidateGVCFsUtils
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import org.apache.logging.log4j.LogManager

class ValidateGVCFs : CliktCommand(help = "Validate GVCF files") {

    private val myLogger = LogManager.getLogger(ValidateGVCFs::class.java)

    val inputDir by option(help = "Full path to input GVCF file directory")
        .required()

    val outputDir by option(help = "Full path to output GVCF file directory")
        .required()

    val referenceFile by option(help = "Full path to reference fasta file")
        .required()

    val correct by option(
        help = "If true, fix incorrect reference sequences in the output GVCF file. " +
                "If false, filter out incorrect reference sequences in the output GVCF file"
    )
        .flag(default = false)

    override fun run() {
        logCommand(this)
        ValidateGVCFsUtils.validateGVCFs(inputDir, outputDir, referenceFile, correct)
    }

}