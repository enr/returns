package com.github.enr.returns.tuple;

public record Pair<F, S>(F first, S second) {

  public static <F, S> Pair<F, S> of(F first, S second) {
    return new Pair<>(first, second);
  }

  @Override
  public String toString() {
    return String.format("Pair(%s, %s)", first, second);
  }
}
