package com.github.enr.returns.tuple;

public record Triplet<F, S, T>(F first, S second, T third) {

  public static <F, S, T> Triplet<F, S, T> of(F first, S second, T third) {
    return new Triplet<>(first, second, third);
  }

  @Override
  public String toString() {
    return String.format("Triplet(%s, %s, %s)", first, second, third);
  }
}
