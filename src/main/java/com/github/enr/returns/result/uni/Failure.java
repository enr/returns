package com.github.enr.returns.result.uni;

import java.util.Objects;
import java.util.Optional;

import com.github.enr.returns.result.Result;
import com.github.enr.testexclusions.Generated;

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

  @Generated
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Failure [message=");
    builder.append(message);
    builder.append(", cause=");
    builder.append(cause);
    builder.append("]");
    return builder.toString();
  }

}
