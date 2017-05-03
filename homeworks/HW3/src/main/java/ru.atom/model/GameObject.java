package ru.atom.model;

/**
 * Any entity of game mechanics
 */
public interface GameObject {
    /**
     * Unique id
     */
    int getId();

    /**
     * Set some actions to object
     * @param action
     */
    void setAction(Actions action);

    /**
     * Get some actions from object
     */
    Actions getAction();
}
