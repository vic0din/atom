package ru.atom.dbhackaton.persons;

import java.util.Random;

/**
 * Created by BBPax on 24.03.17.
 */
public class Token {
    private Long token;
    private User user;

    public Token(User user) {
        this.user = user;
        this.token = System.currentTimeMillis() * 100000L + (long) user.hashCode();
    }

    public Long getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return getToken().toString();
    }
}
