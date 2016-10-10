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

To run the system from the command line execute the following command:
```
./relation_extraction -e <system> -s <sentence> [<args>]`

 -a,--argCand <arg>       Consider also arguments, which are not directly
                          connected to the relation phrase (value:
                          false|true, default: false, extractor: Dep
                          ConIE).
 -c,--constraints <arg>   Use the lexical and syntatic constraint (value:
                          false|true, default: true, extractor: ReVerb).
 -e,--extractor <arg>     The extractor to use: 'ReVerb', 'German ReVerb',
                          or 'Dep ConIE'.
 -f,--minFreq <arg>       Each relation must have at least this number of
                          distinct arguments in a large corpus (value:
                          integer, default: 20/20/0, extractor: all).
 -g,--greedyArg <arg>     Extract relations from sentences with more than
                          two arguments (value: false|true, default:
                          false, extractor: Dep ConIE).
 -m,--morph <arg>         Use a morphology lexicon to determine the case
                          of the first argument (value: false|true,
                          default: true, extractor: German ReVerb).
 -p,--pronouns <arg>      Extract relations, which have just a pronoun as
                          argument (value: false|true, default: false,
                          extractor: Dep ConIE).
 -r,--refVerbs <arg>      Add reflexive verbs to the relation phrase
                          (value: false|true, default: true, extractor:
                          German ReVerb).
 -s,--sent <arg>          The sentence to extract the relations from
                          (value: string).
 -u,--subSent <arg>       Extract relations only from parts of the
                          sentences, which do not contain any comma or
                          conjunction (value: false|true, default: true,
                          extractor: German ReVerb).
 -v,--combVerbs <arg>     Combine the individual parts of a verb (value:
                          false|true, default: true, extractor: German
                          ReVerb).
```
This command extracts the relations from the given sentence using the named extractor with the given arguments. 
For example, you can execute `./relation_extraction -e "Dep ConIE" -s "Dep ConIE ist ein Relationsextraktionssystem."` to obtain the relation `(Dep ConIE # ist # ein Relationsextraktionssystem)`.

To include our system as a library in your own project, please take a look at the example class `RelationExtractionExample` in the `src/main/java/de/hpi/examples` directory.

When running code that calls our system, make sure to increase the Java Virtual Machine heap size by passing the argument `-Xmx2g` to java. Our system loads multiple models into memory. Therefore, not increasing the heap size will slow down the system.
