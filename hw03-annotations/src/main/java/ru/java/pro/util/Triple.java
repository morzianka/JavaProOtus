package ru.java.pro.util;

import org.apache.commons.lang3.tuple.MutableTriple;

import java.util.List;

/**
 * @author Shabunina Anita
 */
public class Triple<T> extends MutableTriple<T, T, T> {

    /**
     * Create a new triple instance.
     *
     * @param left   the left value, may be null
     * @param middle the middle value, may be null
     * @param right  the right value, may be null
     */
    public Triple(T left, T middle, T right) {
        super(left, middle, right);
    }

    public List<T> getList() {
        return List.of(left, middle, right);
    }
}
