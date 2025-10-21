package biokotlin.cli

import com.github.ajalt.clikt.testing.test
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File
class CalcKmerTest : StringSpec({

    val workDir = "${System.getProperty("user.home")}/temp"
    val testingDir = "${workDir}/CalcKmerDistanceTests/"
    val refFile = "${testingDir}/kmer_distance.fa"
    val truthOutPrefix = "${testingDir}/distances_truth"
    val outPrefix = "${testingDir}/distances_test"

    File(testingDir).deleteRecursively()

    // Make the dir first
    File(testingDir).mkdirs()

    //Create the ref File:
    createFastas(refFile)

    // Create true outputs:
    createTruthDistanceTables(truthOutPrefix)

    "Test CalcKmerDistance CLI clikt parameters" {

        // This tests the cli version of the MAFToGVCF converter

        // Test missing reference file
        val calcKmerDistance = CalcKmerDistance()
        val missingRefFile =
            calcKmerDistance.test("--out-prefix ${outPrefix}")
        assertEquals(missingRefFile.statusCode, 1)
        assertEquals(
            "Usage: calc-kmer-distance [<options>]\n" +
                    "\n" +
                    "Error: missing option --input-fasta\n",
            missingRefFile.output
        )

        // Test missing outputFile
        val missingOutputPrefix =
            calcKmerDistance.test("--input-fasta ${refFile}")
        assertEquals(missingOutputPrefix.statusCode, 1)
        assertEquals(
            "Usage: calc-kmer-distance [<options>]\n" +
                    "\n" +
                    "Error: missing option --out-prefix\n",
            missingOutputPrefix.output
        )

        // Test out of bounds kmer size

        shouldThrow <IllegalArgumentException> {
            calcKmerDistance.test("--input-fasta ${refFile} --out-prefix ${outPrefix} --kmer-size 34")
        }


    }

    "Test CalcKmerDistance called from clikt" {

        // This tests the cli version of the MAFToGVCF converter
        val calcKmerDistance = CalcKmerDistance()
        val result =
            calcKmerDistance.test("--input-fasta ${refFile} --out-prefix ${outPrefix}")

        assertEquals(0, result.statusCode)

        // h1 distance
        File(outPrefix + "_h1_distance.tsv").readLines() shouldBe File(truthOutPrefix + "_h1_distance.tsv").readLines()

        // hmany distance
        File(outPrefix + "_hmany_distance.tsv").readLines() shouldBe File(truthOutPrefix + "_hmany_distance.tsv").readLines()

        // copy number difference distance
        File(outPrefix + "_copy_number_difference_distance.tsv").readLines() shouldBe File(truthOutPrefix + "_copy_number_difference_distance.tsv").readLines()

        // copy number count distance
        File(outPrefix + "_copy_number_count_distance.tsv").readLines() shouldBe File(truthOutPrefix + "_copy_number_count_distance.tsv").readLines()


    }

})

/**
 * Function to create a fasta file which will be used to calculate kmer distance
 */
fun createFastas(outputFile: String) {
    File(outputFile).bufferedWriter().use { output ->
        output.write(">seqA\n")
        output.write("GAGGCTAGAAAGCTGCCGTCTCCCTGACCCGTCCGTAGATGACTACCGTAAACTGTGCAA\n" +
                "GAGGCTAGAAAGCTGCCGTCTCCCTGACCCGTCCGTAGATGACTACCGTAAACTGTGCAA\n" +
                "CTGTGCAACCAACGCTGGATAGGCCGTTTTAGGTTAGAAACAGCT\n")

        output.write(">seqB\n")
        output.write("GAGGCTAGAAAGCTGCCGTCTCCCTGACCTGTCCGTAGATGACTACCGTGAACTGTGCAA\n" +
                "CGTGTGGTGCAACCAACGCTGGATAGGCCGTTTTAGGTTAGAATCAGCT\n")

        output.write(">seqC\n")
        output.write("GAGGCTAGAAAGCTGCTCCCTGACCTGTCCGTAGATGACTACCGTGAACTGTGCAACGTG\n" +
                "TGGTGCAACCAACGCAGGATAGGCCGTTTTAGGTTAGAATCAAGCT\n")

    }
}

fun createTruthDistanceTables(outPrefix: String) {
    File(outPrefix + "_h1_distance.tsv").bufferedWriter().use{output ->
        output.write("\tseqA\tseqB\tseqC\n" +
                "seqA\t0.0\t1.1094890510948905\t1.121771217712177\n" +
                "seqB\t1.1094890510948905\t0.0\t0.8558139534883721\n" +
                "seqC\t1.121771217712177\t0.8558139534883721\t0.0\n")
    }

    File(outPrefix + "_hmany_distance.tsv").bufferedWriter().use{output ->
        output.write("\tseqA\tseqB\tseqC\n" +
                "seqA\t0.0\t0.9635036496350365\t1.6088560885608856\n" +
                "seqB\t0.9635036496350365\t0.0\t0.6883720930232559\n" +
                "seqC\t1.6088560885608856\t0.6883720930232559\t0.0\n")
    }

    File(outPrefix + "_copy_number_difference_distance.tsv").bufferedWriter().use{output ->
        output.write("\tseqA\tseqB\tseqC\n" +
                "seqA\t0.0\t0.13138686131386862\t0.0\n" +
                "seqB\t0.13138686131386862\t0.0\t0.0\n" +
                "seqC\t0.0\t0.0\t0.0\n")
    }

    File(outPrefix + "_copy_number_count_distance.tsv").bufferedWriter().use{output ->
        output.write("\tseqA\tseqB\tseqC\n" +
                "seqA\t0.0\t0.26277372262773724\t0.0\n" +
                "seqB\t0.26277372262773724\t0.0\t0.0\n" +
                "seqC\t0.0\t0.0\t0.0\n")
    }
}
