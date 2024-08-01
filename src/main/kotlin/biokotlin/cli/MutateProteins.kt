package biokotlin.cli

import biokotlin.util.MutateProteinsUtils
import biokotlin.util.TypeMutation
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.boolean
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.int
import org.apache.logging.log4j.LogManager

/**
 * This command randomly mutates the protein sequences in a fasta file.
 * The mutations can be deletions, point mutations, or insertions.
 * The output is written to a new fasta file.
 * The mutations can be put in or out of ranges defined by a bedfile.
 */
class MutateProteins : CliktCommand(help = "Mutate Proteins") {

    private val myLogger = LogManager.getLogger(MutateProteins::class.java)

    val inputFasta by option(help = "Full path to input fasta file")
        .required()

    val outputFasta by option(help = "Full path to output fasta file")
        .required()

    val bedfile by option(help = "Full path to bedfile")
        .required()

    val typeMutation by option(help = "Type of mutation")
        .enum<TypeMutation>()
        .default(TypeMutation.POINT_MUTATION)

    val putMutationsInRanges by option(help = "Put mutations in ranges defined by bedfile")
        .boolean()
        .default(true)

    val length by option(help = "Length of the deletion mutation")
        .int()
        .default(5)

    val numMutations by option(help = "Number of point mutations")
        .int()
        .default(10)

    val mutatedIndicesBedfile by option(help = "Full path to bedfile to output mutated indices")

    val randomSeed by option(help = "Random seed")
        .int()
        .default(1234)

    override fun run() {

        MutateProteinsUtils.mutateProteins(
            inputFasta,
            mutatedIndicesBedfile,
            outputFasta,
            bedfile,
            typeMutation,
            length,
            numMutations,
            putMutationsInRanges,
            randomSeed
        )

    }

}