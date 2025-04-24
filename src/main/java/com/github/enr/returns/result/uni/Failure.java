package com.github.enr.returns.result.uni;

import java.util.Objects;
import java.util.Optional;

import com.github.enr.returns.result.ErrorKind;
import com.github.enr.returns.result.Result;
import com.github.enr.returns.result.UnclassifiedError;
import com.github.enr.testexclusions.Generated;

public final class Failure<T> implements Result<T> {

  private final ErrorKind kind;
  private final String message;
  private final Throwable cause;

  public Failure(ErrorKind kind, Throwable cause, String message) {
    this.kind = Objects.requireNonNull(kind, "kind must not be null");
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

  @Override
  public boolean is(Class<? extends ErrorKind> errorKind) {
    // kind is always not null
    return kind.getClass().isAssignableFrom(errorKind);
  }

  @Override
  public boolean isFailureOfKind(Class<? extends ErrorKind> errorKind) {
    return is(errorKind);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <K extends ErrorKind> K kind() {
    return (K) kind;
  }

  @Override
  public boolean isUnclassified() {
    return UnclassifiedError.INSTANCE.equals(kind);
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
