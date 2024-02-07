package com.github.enr.returns.tuple;

import java.util.NoSuchElementException;

final class Left<L, R> implements Either<L, R> {
  private final L value;

  public Left(L value) {
    this.value = value;
  }

  @Override
  public boolean isLeft() {
    return true;
  }

  @Override
  public L left() {
    return value;
  }

  @Override
  public R right() {
    throw new NoSuchElementException("Tried to get right value from a Left");
  }
}
