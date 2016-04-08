package edu.washington.cs.knowitall.extractor.mapper;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.knowitall.nlp.ChunkedSentence;

/**
 * Given a set of <code>ChunkedSentences</code>s from the same sentence, merges those sentences
 * that are next to each other or overlapping. For example, given the sentence "He wants to go to
 * the store" and the relations "wants to" and "go to", returns "wants to go to".
 *
 * @author afader
 */
public class SentenceMergeOverlappingMapper extends Mapper<ChunkedSentence> {

    private List<ChunkedSentence> mergeOverlapping(List<ChunkedSentence> sentences) {
        List<ChunkedSentence> result = new ArrayList<>(sentences.size());

        if (sentences.size() > 1) {
            for (int i = 0; i < sentences.size() - 1; i++) {
                ChunkedSentence sent1 = sentences.get(i);
                boolean isContained = false;

                for (int j = i + 1; j < sentences.size(); j++) {
                    ChunkedSentence sent2 = sentences.get(j);

                    if (isContainedIn(sent1, sent2)) {
                        isContained = true;
                        break;
                    }
                }

                if (!isContained) {
                    result.add(sent1);
                }
            }
            result.add(sentences.get(sentences.size() - 1));
            return result;
        } else {
            return sentences;
        }
    }

    /**
     * @param sent1 sentence 1
     * @param sent2 sentence 2
     * @return true, if sent1 is contained in or equal to sent2
     */
    private boolean isContainedIn(ChunkedSentence sent1, ChunkedSentence sent2) {
        return sent2.getTokens().containsAll(sent1.getTokens()) &&
               sent2.getChunkTags().containsAll(sent1.getChunkTags()) &&
               sent2.getPosTags().containsAll(sent1.getPosTags());
    }

    @Override
    protected Iterable<ChunkedSentence> doMap(Iterable<ChunkedSentence> sents) {
        List<ChunkedSentence> sentsList = new ArrayList<>();
        Iterables.addAll(sentsList, sents);

        if (sentsList.size() > 1) {
            return mergeOverlapping(sentsList);
        } else {
            return sentsList;
        }
    }

}
