package com.github.enr.returns.tuple;

public sealed interface Either<L, R> permits Left, Right {

  default boolean isLeft() {
    return false;
  }

  default boolean isRight() {
    return false;
  }

  L left();

  R right();

  public static <L, R> Either<L, R> left(L left) {
    return new Left<>(left);
  }

  public static <L, R> Either<L, R> right(R right) {
    return new Right<>(right);
  }
}
