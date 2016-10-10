package de.hpi.sequence;

import java.util.Collection;

/**
 * Represents a layered sequence of strings. An example of a layered sequence is a POS-tagged
 * sentence, which has a word layer, and a POS tag layer.
 *
 * This interface defines the basic functionality expected from a layered sequence, namely the
 * ability to access the value of the sequence at a given index of the given layer.
 *
 * @author afader
 */
public interface LayeredSequence {

    /**
     * @param layerName the layer name
     * @param index     the index
     * @return the value at the given position.
     */
    public String get(String layerName, int index);

    /**
     * @return the layer names
     */
    public Collection<String> getLayerNames();

    /**
     * @param layerName the layer name
     * @return true if this sequence has a layer with the given name
     */
    public boolean hasLayer(String layerName);

    /**
     * @return the number of layers
     */
    public int getNumLayers();

    /**
     * @return the length of the sequence
     */
    public int getLength();

}
