# Relation Extraction on German texts

This project is based on https://github.com/knowitall/reverb.
It contains three approaches, which extract binary relations from German sentences:
* ReVerb: The ReVerb system (https://github.com/knowitall/reverb) adapted to work on German texts.
* German ReVerb: The ReVerb system adapted to work on German texts and enhanced by rules specialized to the German language.
* Dep ConIE: A dependency-based approach for German texts achieving the best performance results.

Thereby, the relation types are not specified in advanced, so that any kind of relations are found.
A binary relations consists of three parts: arg1, rel, arg2.

# Requirements

To successfully run ReVerb, you need 
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

To include ReVerb as a library in your own project, please take a look at the example class ReVerbExample in the `src/main/java/edu/washington/cs/knowitall/examples` directory.

When running code that calls ReVerb, make sure to increase the Java Virtual Machine heap size by passing the argument `-Xmx2g` to java. ReVerb loads multiple models into memory, and will be significantly slower if the heap size is not large enough.
