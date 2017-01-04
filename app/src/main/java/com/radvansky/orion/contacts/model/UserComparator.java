package com.radvansky.orion.contacts.model;

import java.util.Comparator;

/**
 * Created by tomasradvansky on 04/01/2017.
 */

public class UserComparator implements Comparator<User>
{
    public int compare(User left, User right) {
        return left.name.compareTo(right.name);
    }
}