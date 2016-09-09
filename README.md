# From text to facts: Relation Extraction on German Websites

This repository holds a collection of three Open Information Extraction approaches for the German language:

**ReVerb**
This system is based on the ReVerb system available under https://github.com/knowitall/reverb, which is published under the ReVerb Software License Agreement. We adapted the system to the German language.

**German ReVerb**
German ReVerb is an extension of the previously mentioned system also based on the ReVerb system available under https://github.com/knowitall/reverb. This system is specialized to the German language by adding rules dealing with specific aspects of the German language to the system.

**Dep ConIE**
This system is a new approach based on dependency parsing. It yields the best results.

# Requirements

To successfully run one of the above systems, you need
* Java JDK 1.8 or later
* Maven 3.0.0 or later

# Set Up

To set up the system locally, you have to 

1. Download and install TreeTagger (http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/). Set the environment variable 'TREETAGGER_HOME' to the installation directory.
2.  Put the following models to the resource folder (src/main/resources):
 * 	`de-sent.bin`, `de-pos-maxent.bin` from http://opennlp.sourceforge.net/models-1.5/
 * 	`lemma-ger-3.6.model` from the archive `ger-tagger+lemmatizer+morphology+graph-based-3.6+.tgz`, which can be downloaded from https://code.google.com/archive/p/mate-tools/downloads
3. Download `Vollformenlexikon 2011-07-22` from http://www.danielnaber.de/morphologie/ and put the extracted `morphy-export-20110722.xml`-file to the resource folder (src/main/resources)
4. Download and install ParZu (https://github.com/rsennrich/ParZu). Set the environment variable 'PARZU_HOME' to the installation directory.
5. Download the compact version of Zmorge from http://kitt.ifi.uzh.ch/kitt/zmorge/ and put it to the resource folder (src/main/resources).

# Running the system

Run the command 
`echo "Dieser Aufruf ist ein Test." | ./relation_extraction`
to obtain the relations contained in the given sentence.

To include one of our systems as a library in your own project, please take a look at the example class `RelationExtractionExample` in the `src/main/java/edu/washington/cs/knowitall/examples` directory.

When running code that calls one of our systems, make sure to increase the Java Virtual Machine heap size by passing the argument `-Xmx2g` to java. Our systems load multiple models into memory. Therefore, not increasing the heap size will slow down the systems.
