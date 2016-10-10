package edu.washington.cs.knowitall.util;

import com.google.common.collect.Iterables;
import edu.washington.cs.knowitall.nlp.extraction.chunking.ChunkedBinaryExtraction;
import edu.washington.cs.knowitall.nlp.extraction.dependency_parse_tree.TreeBinaryExtraction;
import org.apache.commons.cli.*;
import java.io.IOException;


/**
 * A command line wrapper for all the extractors. Run with -h to see the usage
 * information.
 */
public class CommandLineExtractor {

    public static void main(String[] args) {
        Options options = new Options();

        Option extractor = new Option("e", "extractor", true, "The extractor to use: 'ReVerb', 'German ReVerb', or 'Dep ConIE'.");
        extractor.setRequired(true);
        options.addOption(extractor);

        Option sentenceOpt = new Option("s", "sent", true, "The sentence to extract the relations from (value: string).");
        sentenceOpt.setRequired(true);
        options.addOption(sentenceOpt);

        options.addOption("f", "minFreq", true, "Each relation must have at least this number of distinct arguments in a large corpus (value: integer, default: 20/20/0, extractor: all).");

        /**
         * Parameter for ReVerb
         */

        options.addOption("c", "constraints", true, "Use the lexical and syntatic constraint (value: false|true, default: true, extractor: ReVerb).");

        /**
         * Parameter for German ReVerb
         */

        options.addOption("v", "combVerbs", true, "Combine the individual parts of a verb (value: false|true, default: true, extractor: German ReVerb).");
        options.addOption("r", "refVerbs", true, "Add reflexive verbs to the relation phrase (value: false|true, default: true, extractor: German ReVerb).");
        options.addOption("m", "morph", true, "Use a morphology lexicon to determine the case of the first argument (value: false|true, default: true, extractor: German ReVerb).");
        options.addOption("u", "subSent", true, "Extract relations only from parts of the sentences, which do not contain any comma or conjunction (value: false|true, default: true, extractor: German ReVerb).");

        /**
         * Parameter for Dep ConIE
         */

        options.addOption("a", "argCand", true, "Consider also arguments, which are not directly connected to the relation phrase (value: false|true, default: false, extractor: Dep ConIE).");
        options.addOption("g", "greedyArg", true, "Extract relations from sentences with more than two arguments (value: false|true, default: false, extractor: Dep ConIE).");
        options.addOption("p", "pronouns", true, "Extract relations, which have just a pronoun as argument (value: false|true, default: false, extractor: Dep ConIE).");


        /**
         * Parse the arguments
         */

        CommandLineParser parser = new GnuParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("relation extraction", options);

            System.exit(1);
            return;
        }

        /**
         * Extract the relations
         */

        String extractorName = cmd.getOptionValue("extractor");
        switch (extractorName) {
            case "ReVerb":
                try {
                    extractReVerb(cmd);
                } catch (IOException e) {
                    System.out.println("An error occurred during the extraction.");
                    System.out.println(e.getMessage());
                }
                break;
            case "German ReVerb":
                try {
                    extractGermanReVerb(cmd);
                } catch (IOException e) {
                    System.out.println("An error occurred during the extraction.");
                    System.out.println(e.getMessage());
                }
                break;
            case "Dep ConIE":
                try {
                    extractDepConIE(cmd);
                } catch (IOException e) {
                    System.out.println("An error occurred during the extraction.");
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("The extractor " + extractorName + " is not valid!");
                System.out.println("Use either 'ReVerb', 'German ReVerb', or 'Dep ConIE'!");
                formatter.printHelp("utility-name", options);
        }
    }

    private static void extractReVerb(CommandLine cmd) throws IOException {
        String minFreq = cmd.getOptionValue("minFreq", "20");
        String constraints = cmd.getOptionValue("constraints", "true");
        String sentence = cmd.getOptionValue("sent");

        System.out.println("System:     ReVerb (constraints: " + constraints + ", minimum frequency: " + minFreq + ")");
        System.out.println("Sentence:   " + sentence);
        System.out.println("");

        ReVerb reverb = new ReVerb(false, Integer.valueOf(minFreq), Boolean.valueOf(constraints));
        Iterable<ChunkedBinaryExtraction> relations = reverb.extractRelationsFromString(sentence);

        if (Iterables.isEmpty(relations)) {
            System.out.println("No extractions found.");
            return;
        }

        StringBuilder str = new StringBuilder();
        for (ChunkedBinaryExtraction extr : relations) {
            str.append("Extraction: ").append(extr).append("\n");
        }
        System.out.println(str);
    }

    private static void extractGermanReVerb(CommandLine cmd) throws IOException {
        String minFreq = cmd.getOptionValue("minFreq", "0");
        String combineVerbs = cmd.getOptionValue("combVerbs", "false");
        String reflexiveVerbs = cmd.getOptionValue("refVerbs", "false");
        String moprohlogyLexicon = cmd.getOptionValue("morph", "false");
        String subsentences = cmd.getOptionValue("subSent", "false");
        String sentence = cmd.getOptionValue("sent");

        System.out.println("System:     German ReVerb (minimum frequency: " + minFreq + ", combine verbs: " + combineVerbs + ", reflexive verbs: " + reflexiveVerbs + ", moprohlogical lexicon: " + moprohlogyLexicon + ", subsentences: " + subsentences + ")");
        System.out.println("Sentence:   " +sentence);
        System.out.println("");

        GermanReVerb germanReVerb = new GermanReVerb(false, Integer.valueOf(minFreq), true, Boolean.valueOf(combineVerbs), Boolean.valueOf(reflexiveVerbs), Boolean.valueOf(moprohlogyLexicon), Boolean.valueOf(subsentences));
        Iterable<ChunkedBinaryExtraction> relations = germanReVerb.extractRelationsFromString(sentence);

        if (Iterables.isEmpty(relations)) {
            System.out.println("No extractions found.");
            return;
        }

        StringBuilder str = new StringBuilder();
        for (ChunkedBinaryExtraction extr : relations) {
            str.append("Extraction: ").append(extr).append("\n");
        }
        System.out.println(str);
    }

    private static void extractDepConIE(CommandLine cmd) throws IOException {
        String minFreq = cmd.getOptionValue("minFreq", "0");
        String argumentCandidates = cmd.getOptionValue("argCand", "false");
        String greedyArguments = cmd.getOptionValue("greedyArg", "false");
        String pronoun = cmd.getOptionValue("pronouns", "false");
        String sentence = cmd.getOptionValue("sent");

        System.out.println("System:     Dep ConIE (minimum frequency: " + minFreq + ", argument candidates: " + argumentCandidates + ", greedy arguments: " + greedyArguments + ", pronouns: " + pronoun + ")");
        System.out.println("Sentence:   " + sentence);
        System.out.println("");

        DepConIE depConIE = new DepConIE(false, Integer.valueOf(minFreq), Boolean.valueOf(argumentCandidates), Boolean.valueOf(pronoun), Boolean.valueOf(greedyArguments));
        Iterable<TreeBinaryExtraction> relations = depConIE.extractRelationsFromString(sentence);

        if (Iterables.isEmpty(relations)) {
            System.out.println("No extractions found.");
            return;
        }

        StringBuilder str = new StringBuilder();
        for (TreeBinaryExtraction extr : relations) {
            str.append("Extraction: ").append(extr).append("\n");
        }
        System.out.println(str);
    }

}
