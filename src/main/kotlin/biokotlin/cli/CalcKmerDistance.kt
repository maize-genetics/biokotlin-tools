package biokotlin.cli

import biokotlin.kmer.kmerDistanceMatrix
import biokotlin.kmer.printMatrixToFile
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import org.apache.logging.log4j.LogManager
import java.io.File

class CalcKmerDistance: CliktCommand(help = "Calculate Kmer Distance values between sequences") {

    private val myLogger = LogManager.getLogger(MergeGVCFs::class.java)

    val inputFasta by option(help = "Path to fasta file with sequences to compare")
        .required()

    val outPrefix by option(help = "Prefix to use for the output tables. Four tables will be created")
        .required()

    val kmerSize by option(help = "kmer length to use. Default 21.").int().default(21)



    override fun run() {

        logCommand(this)

        // Checks to ensure that the input directory exists
        require(File(inputFasta).isFile) { "Input fasta does not exist: $inputFasta" }

        require(kmerSize in 2..31) {"Kmer size must be between 2 and 31: $kmerSize"}

        myLogger.info("Calculating distance matrices")
        val distanceMatrices = kmerDistanceMatrix(inputFasta, kmerSize)

        myLogger.info("Writing files")

        printMatrixToFile(distanceMatrices.seqIDs, distanceMatrices.h1Count, outPrefix + "_h1_distance.tsv")
        printMatrixToFile(distanceMatrices.seqIDs, distanceMatrices.hManyCount, outPrefix + "_hmany_distance.tsv")
        printMatrixToFile(distanceMatrices.seqIDs, distanceMatrices.copyNumberDifference, outPrefix + "_copy_number_difference_distance.tsv")
        printMatrixToFile(distanceMatrices.seqIDs, distanceMatrices.copyNumberCount, outPrefix + "_copy_number_count_distance.tsv")

    }




}