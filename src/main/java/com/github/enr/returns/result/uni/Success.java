package com.github.enr.returns.result.uni;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import com.github.enr.returns.result.Result;
import com.github.enr.testexclusions.Generated;

public final class Success<T> implements Result<T> {

  private final T value;

  public Success(T value) {
    this.value = Objects.requireNonNull(value, "Value must not be null");
  }

  @Override
  public boolean isSuccessful() {
    return true;
  }

  /**
   *
   * @return the non-null value held by this {@code Result}
   */
  @Override
  public T unwrap() {
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

  @Generated
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Success [");
    builder.append(value);
    builder.append("]");
    return builder.toString();
  }

}
