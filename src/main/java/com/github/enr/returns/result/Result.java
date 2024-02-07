package com.github.enr.returns.result;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public sealed interface Result<T> permits Success, Failure, Skip, CompositeResult {

  default T unwrap() {
    throw new NoSuchElementException("No value present");
  }

  default boolean isSuccessful() {
    return false;
  }

  default boolean isFailed() {
    return false;
  }

  default boolean isSkipped() {
    return false;
  }

  default boolean isComposite() {
    return false;
  }

  default String explanation() {
    return null;
  }

  default Optional<Throwable> cause() {
    return Optional.empty();
  }

  /*
   * Returns true if is Err and the failure value matches the given predicate.
   */
  default boolean isErrorAnd(Predicate<Failure<T>> predicate) {
    if (!isFailed()) {
      return false;
    }
    if (this instanceof Failure<T> fail) {
      return predicate.test(fail);
    }
    return false;
  }

  /**
   * Produces an Optional<T> containing the value when this is a successful result and discarding the
   * error, if any.
   */
  default Optional<T> toOptional() {
    return Optional.empty();
  }

  /**
   * Return the value if the result is marked as "successful", otherwise return {@code fallback}.
   *
   * @param other the value to be returned if result is not success, may be null
   * @return the value, if present, otherwise {@code fallback}
   */
  default T orElse(T fallback) {
    return isSuccessful() ? unwrap() : fallback;
  }

  default T orFail(String errorMessage) {
    if (isSuccessful()) {
      return unwrap();
    }
    throw new IllegalStateException(errorMessage);
  }

  default void ifSuccess(Consumer<T> consumer) {}

  public static Result<Empty> success() {
    return new Success<>(Empty.INSTANCE);
  }

  public static <T> Result<T> success(T value) {
    return new Success<>(value);
  }

  public static <T> Result<T> skip(String reason) {
    return new Skip<>(reason);
  }

  public static <T> Result<T> failingWithMessage(String errorMessage) {
    return new Failure<>(errorMessage, null);
  }

  public static <T> Result<T> failingWithMessage(String fmt, Object... args) {
    String errorMessage = String.format(fmt, args);
    return new Failure<>(errorMessage, null);
  }

  public static <T> Result<T> failingWithCause(Throwable cause) {
    Throwable c = Objects.requireNonNull(cause, "cause must not be null");
    String errorMessage = c.getMessage() == null ? "generic error" : c.getMessage();
    return new Failure<>(errorMessage, null);
  }
}
