package me.kayaba.guilds.impl.util;

import org.apache.commons.lang.*;

import java.util.*;

public class NonNullArrayList<E> extends ArrayList<E> {
    @Override
    public boolean add(E e) {
        Validate.notNull(e);
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        Validate.notNull(element);
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            Validate.notNull(e);
        }

        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        for (E e : c) {
            Validate.notNull(e);
        }

        return super.addAll(index, c);
    }
}
