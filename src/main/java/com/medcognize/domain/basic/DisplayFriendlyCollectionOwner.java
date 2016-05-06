package com.medcognize.domain.basic;

import java.util.List;

public interface DisplayFriendlyCollectionOwner {
    // needs to be able to handle the addition of an already existing item
    // in which case we just copy the listed properties
    public void add(DisplayFriendly ownedItem);

    public void remove(DisplayFriendly ownedItem);

    public <T extends DisplayFriendly> List<T> getAll(Class<T> clazz);
}
