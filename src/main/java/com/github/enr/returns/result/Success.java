package com.github.enr.returns.result;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

public final class Success<T> implements Result<T> {

  private final T value;

  public Success(T value) {
    this.value = value;
  }

  @Override
  public boolean isSuccessful() {
    return true;
  }

  /**
   * If a value is present, returns the value, otherwise throws {@code NoSuchElementException}.
   *
   * @return the non-null value held by this {@code Result}
   * @throws NoSuchElementException if there is no value present
   * @see Optional#isPresent()
   */
  @Override
  public T unwrap() {
    if (value == null) {
      throw new NoSuchElementException("No value present");
    }
    return value;
  }

  @Override
  public void ifSuccess(Consumer<T> consumer) {
    consumer.accept(value);
  }

  @Override
  public Optional<T> toOptional() {
    return Optional.ofNullable(value);
  }
}
