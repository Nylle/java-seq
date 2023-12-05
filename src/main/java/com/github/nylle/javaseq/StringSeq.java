package com.github.nylle.javaseq;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

public class StringSeq extends ASeq<Character> implements ISeq<Character> {

    private final CharSequence s;
    private final int i;

    StringSeq(CharSequence s, int i) {
        if(s == null || s.isEmpty()) {
            throw new IllegalArgumentException("string is null or empty");
        }
        this.s = s;
        this.i = i;
    }

    @Override
    public Character first() {
        return s.charAt(i);
    }

    @Override
    public ISeq<Character> rest() {
        return i + 1 < s.length() ? new StringSeq(s, i + 1) : ISeq.of();
    }

    @Override
    public boolean isRealized() {
        return true;
    }

    @Override
    public ISeq<Character> filter(Predicate<? super Character> pred) {
        if (pred.test(first())) {
            return rest().filter(pred).cons(first());
        } else {
            return rest().filter(pred);
        }
    }

    @Override
    public <R> ISeq<R> map(Function<? super Character, ? extends R> f) {
        return ISeq.cons(f.apply(first()), rest().map(f));
    }

    @Override
    public <R> ISeq<R> mapcat(Function<? super Character, ? extends Iterable<? extends R>> f) {
        var result = new ArrayList<R>();
        f.apply(first()).forEach(x -> result.add(x));
        return ISeq.concat(result, rest().mapcat(f));
    }

    @Override
    public ISeq<Character> cons(Character x) {
        return new StringSeq(x.toString() + s.subSequence(i, s.length()), 0);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}