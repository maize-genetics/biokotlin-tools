
# BioKotlin Tools README

## Overview
**BioKotlin Tools** is a command-line toolkit designed for genomic data processing and analysis. It includes a variety of utilities to handle and manipulate Variant Call Format (VCF) and Mutation Annotation Format (MAF) files. This toolkit is particularly useful for creating, validating, merging, and analyzing genomic variant data.

## Installation
To install BioKotlin Tools, download the package and ensure all dependencies are met. The tools rely on external software like `bgzip` and `bcftools` for specific operations. Make sure these tools are installed and accessible in your system's PATH.

## Usage
The main entry point for BioKotlin Tools is the `biokotlin-tools` executable. The general usage pattern is:

```
biokotlin-tools [<options>] <command> [<args>]...
```

### Global Options
- `--version`: Show the version of BioKotlin Tools and exit.
- `-h, --help`: Show help message and exit.

## Commands
BioKotlin Tools offers several commands, each with specific options and arguments:

### 1. maf-to-gvcf-converter
Converts a MAF file to a GVCF file.

**Usage:**
```
biokotlin-tools maf-to-gvcf-converter [<options>]
```

**Options:**
- `--reference-file=<text>`: Path to the local Reference FASTA file.
- `--maf-file=<text>`: MAF file to be converted to GVCF.
- `-o, --output-file=<text>`: Name for the output GVCF file.
- `--sample-name=<text>`: Sample name to be used in the GVCF file.
- `-f`: Fill in gaps with reference blocks if the MAF file does not cover the entire genome.
- `--two-gvcfs`: Output just the GT flag.
- `--del-as-symbolic`: Represent large deletions with the symbolic allele `<DEL>`.
- `--out-just-gt`: Indicates input MAF was created from a diploid alignment.
- `-c / --compress-off`: Do not compress and index the output file.
- `--output-type=<text>`: Type of dataset to export (gvcf or vcf).
- `--max-deletion-size=<int>`: Replace deletions longer than this size with symbolic alleles.
- `--anchorwave-legacy`: Enable this option if the MAF was created prior to Anchorwave version 1.2.3.

### 2. validate-gvcfs
Validates GVCF files for consistency and correctness.

**Usage:**
```
biokotlin-tools validate-gvcfs [<options>]
```

**Options:**
- `--input-dir=<text>`: Full path to the input GVCF file directory.
- `--output-dir=<text>`: Full path to the output GVCF file directory.
- `--reference-file=<text>`: Full path to the reference FASTA file.
- `--correct`: Correct or filter out incorrect reference sequences in the output GVCF file.

### 3. merge-gvcfs
Merges multiple GVCF files into a single VCF file.

**Usage:**
```
biokotlin-tools merge-gvcfs [<options>]
```

**Options:**
- `--input-dir=<text>`: Full path to the input GVCF file directory.
- `--output-file=<text>`: Full path to the output VCF file.

### 4. validate-vcfs
Validates (G)VCF files in a directory.

**Usage:**
```
biokotlin-tools validate-vcfs [<options>]
```

**Options:**
- `--input-dir=<text>`: Full path to the input (G)VCF file directory.

### 5. mutate-proteins
Mutates proteins based on input parameters.

**Usage:**
```
biokotlin-tools mutate-proteins [<options>]
```

**Options:**
- `--input-fasta=<text>`: Full path to the input FASTA file.
- `--output-fasta=<text>`: Full path to the output FASTA file.
- `--bedfile=<text>`: Full path to the BED file specifying regions.
- `--type-mutation=(DELETION|POINT_MUTATION|INSERTION)`: Type of mutation.
- `--put-mutations-in-ranges=true|false`: Apply mutations within BED file ranges.
- `--length=<int>`: Length of the deletion mutation.
- `--num-mutations=<int>`: Number of point mutations.
- `--mutated-indices-bedfile=<text>`: Full path to BED file for outputting mutated indices.
- `--random-seed=<int>`: Seed for random number generation.
