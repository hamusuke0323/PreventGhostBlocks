package com.hamusuke.preventghost.util;

import com.google.common.base.Objects;

public class Pair<FIRST, SECOND> {
    private final FIRST first;
    private final SECOND second;

    public Pair(final FIRST first, final SECOND second) {
        this.first = first;
        this.second = second;
    }

    public FIRST getFirst() {
        return this.first;
    }

    public SECOND getSecond() {
        return this.second;
    }

    @Override
    public String toString() {
        return "(" + this.first + ", " + this.second + ")";
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Pair<?, ?>)) {
            return false;
        }

        final Pair<?, ?> other = (Pair<?, ?>) obj;
        return java.util.Objects.equals(this.first, other.first) && java.util.Objects.equals(this.second, other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.first, this.second);
    }

    public static <FIRST, SECOND> Pair<FIRST, SECOND> of(final FIRST first, final SECOND second) {
        return new Pair<>(first, second);
    }
}
