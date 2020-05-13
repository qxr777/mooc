package edu.whut.cs.jee.mooc.common.persistence;

public interface Converter<A, B> {

    B doForward(A a);

    A doBackward(B b);
}
