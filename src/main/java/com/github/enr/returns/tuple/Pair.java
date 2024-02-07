package com.github.enr.returns.tuple;

public class Pair<F, S> {

  public static <F, S> Pair<F, S> of(F first, S second) {
    return new Pair<>(first, second);
  }

  private final F first;
  private final S second;

  private Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  public F first() {
    return first;
  }

  public S second() {
    return second;
  }

  @Override
  public String toString() {
    return String.format("(%s, %s)", first(), second());
  }
}
