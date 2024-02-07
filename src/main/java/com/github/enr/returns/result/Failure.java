package com.github.enr.returns.result;

import java.util.Objects;
import java.util.Optional;

public final class Failure<T> implements Result<T> {

  private final String message;
  private final Throwable cause;

  public Failure(String message, Throwable cause) {
    this.message = Objects.requireNonNull(message, "message must not be null");
    this.cause = cause;
  }

  @Override
  public boolean isFailed() {
    return true;
  }

  @Override
  public Optional<Throwable> cause() {
    return Optional.ofNullable(cause);
  }

  @Override
  public String explanation() {
    return message;
  }
}
