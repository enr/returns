package com.github.enr.returns.tuple;

import java.util.NoSuchElementException;

record Right<L, R>(R right) implements Either<L, R> {

  @Override
  public boolean isRight() {
    return true;
  }

  @Override
  public L left() {
    throw new NoSuchElementException("Tried to get left value from a Right");
  }

  @Override
  public String toString() {
    return String.format("Right(%s)", right);
  }
}
