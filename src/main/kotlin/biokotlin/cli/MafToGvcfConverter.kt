package biokotlin.cli

import biokotlin.genome.MAFToGVCF
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int

/**
 * This class is a subclass of CliktCommand and is used to create a GVCF file from a MAF file.
 * It provides users a means of access to BioKotlin's MAFToGVCF functionality from the command line.
 *
 * Any file restrictions from the MAFToGVCF class are relevant here.
 */
class MafToGvcfConverter : CliktCommand(help = "Create a GVCF file from a MAF file") {
    val referenceFile by option(help = "Path to local Reference FASTA file")
        .required()

    val mafFile by option(help = "MAF file to be converted to GVCF")
        .required()

    val outputFile by option("-o", "--output-file", help = "Name for output GVCF file ")
        .required()

    val sampleName by option(help = "sampleName to be used in the GVCF file")
        .required()

    // For the flag options that default to false:  if you include the flag, the value is true, if you do not include the flag, the value is false
    val fillGaps by option(
        "-f",
        help = "If true, and the maf file does not fully cover the reference genome, any gaps\n" +
                " *     in coverage will be filled in with reference blocks."
    )
        .flag(default = false)

    val twoGvcfs by option(help = "Output just the GT flag.  If set to false will output DP, AD and PL fields")
        .flag(default = false)

    val delAsSymbolic by option(help = "If true, deletions larger than maxDeletionSize (see below) will be represented with the symbolic allele <DEL>.")
        .flag(default = false)

    val outJustGT by option(
        help = "If true, it indicates the input maf was created from a diploid alignment and should\n" +
                " *     be used to create two separate gVCF files"
    )
        .flag(default = false)

    // For the flag options that default to TRUE- if you include the flag, the value is TRUE. if you do not include the flag, the value is true
    // You must use the secondary name (here it is the --compress-off flag) to set the value to false.
    val compressAndIndex by option(
        "-c",
        help = "Run bgzip and bcftools index -c on the output file. if bgzip and bcftools are not on the system path, this will fail"
    )
        .flag("--compress-off", default = true)

    val outputType by option(help = "Type of dataset to export: choices are gvcf or vcf")
        .default("gvcf")

    val maxDeletionSize by option(help = "If del-as-symbolic is true, replace deletions longer than this size with symbolic alleles")
        .int()
        .default(0)

    val anchorwaveLegacy by option(help = "Enable this option if the MAF was created prior to Anchorwave version 1.2.3.  Otherwise the ASM coordinates will not be correct. ")
        .flag(default = false)

    override fun run() {
        println("LCJ begin run: fillGaps = $fillGaps, twoGvcfs = $twoGvcfs, outJustGT = $outJustGT, compressAndIndex = $compressAndIndex, delAsSymbolic = $delAsSymbolic,  outputType = $outputType")
        val outputType = try {
            MAFToGVCF.OUTPUT_TYPE.valueOf(outputType)
        } catch (exc: IllegalArgumentException) {
            // Handle the case where the user input doesn't match any enum value
            throw IllegalArgumentException("Invalid output type: $outputType . Please specify either gvcf or vcf or leave blank for default of gvcf")
        }

        println("Creating GVCF file from MAF file vai MAFToGVCF.createGVCFfromMAF()")
        MAFToGVCF().createGVCFfromMAF(
            mafFile,
            referenceFile,
            outputFile,
            sampleName,
            fillGaps,
            twoGvcfs,
            outJustGT,
            outputType,
            compressAndIndex,
            delAsSymbolic,
            maxDeletionSize,
            anchorwaveLegacy
        )

    }
}