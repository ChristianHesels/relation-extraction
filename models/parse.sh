#!/bin/sh

HOME=/opt/GermanParser
SENT=$1

echo $SENT | 
perl $HOME/cmd/tokenize.perl |
perl -ne 'print;print "\n" if /^[.\!\?]$/' |
$HOME/bin/bitpar -ts '()' -s TOP -v $HOME/Tiger/grammar $HOME/Tiger/lexicon -u $HOME/Tiger/open-class-tags -w $HOME/Tiger/wordclass.txt | $HOME/bin/tmod -p -u
