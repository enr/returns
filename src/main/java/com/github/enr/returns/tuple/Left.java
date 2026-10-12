package com.github.enr.returns.tuple;

import java.util.NoSuchElementException;

record Left<L, R>(L left) implements Either<L, R> {

  @Override
  public boolean isLeft() {
    return true;
  }

  @Override
  public R right() {
    throw new NoSuchElementException("Tried to get right value from a Left");
  }

  @Override
  public String toString() {
    return String.format("Left(%s)", left);
  }
}
