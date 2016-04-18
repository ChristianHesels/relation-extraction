# German ReVerb

This project is forked from https://github.com/knowitall/reverb.

German ReVerb extracts binary relations from German sentences.
The relation type is not specified in advanced, so that any kind of relations are found.
A binary relations consists of three parts: argument1, relation, arguemnt2.
Arugment 1 and 2 are always some kind of nouns. 
The relations is based on a verb.

# Requirements

To successfully run ReVerb, you need 
* Java JDK 1.8 or later
* Maven 3.0.0 or later

# Set Up

To set up ReVerb locally, you have to 

1. Download and install TreeTagger (http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/). Set the environment variable 'TREETAGGER_HOME' to the installation directory.
2.  Put the following models to the resource folder (src/main/resources):
 * 	`de-token.bin`, `de-sent.bin`, `de-pos-maxent.bin` from http://opennlp.sourceforge.net/models-1.5/
 * 	`parser-ger-3.6.model`, `tag-ger-3.6.model`, `morphology-ger-3.6.model`, `lemma-ger-3.6.model` from the archive `ger-tagger+lemmatizer+morphology+graph-based-3.6+.tgz`, which can be downloaded from https://code.google.com/archive/p/mate-tools/downloads
3. Download `Vollformenlexikon 2011-07-22` from http://www.danielnaber.de/morphologie/ and put the extracted `morphy-export-20110722.xml`-file to the resource folder (src/main/resources)

# Running ReVerb

To include ReVerb as a library in your own project, please take a look at the example class ReVerbExample in the `src/main/java/edu/washington/cs/knowitall/examples` directory.

When running code that calls ReVerb, make sure to increase the Java Virtual Machine heap size by passing the argument `-Xmx2g` to java. ReVerb loads multiple models into memory, and will be significantly slower if the heap size is not large enough.
