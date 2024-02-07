package com.github.enr.returns.tuple;

import java.util.NoSuchElementException;

final class Right<L, R> implements Either<L, R> {
  private final R value;

  public Right(R value) {
    this.value = value;
  }

  @Override
  public boolean isRight() {
    return true;
  }

  @Override
  public L left() {
    throw new NoSuchElementException("Tried to get left value from a Right");
  }

  @Override
  public R right() {
    return value;
  }

}
