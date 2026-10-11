package com.github.enr.returns.result;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.enr.returns.result.multi.CompositeResult;
import com.github.enr.returns.result.uni.Failure;
import com.github.enr.returns.result.uni.Skip;
import com.github.enr.returns.result.uni.Success;

public sealed interface Result<T> permits Success, Failure, Skip, CompositeResult {

  default T unwrap() {
    throw new NoSuchElementException("No value present");
  }

  default boolean isSuccessful() {
    return false;
  }

  default boolean isUnsuccessful() {
    return !isSuccessful();
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
   * @param fallback the value to be returned if result is not success, may be null
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

  default boolean is(Class<? extends ErrorKind> errorKind) {
    return false;
  }

  /*
   * shortcut for isFailed() && is(errorKind)
   */
  default boolean isFailureOfKind(Class<? extends ErrorKind> errorKind) {
    return false;
  }

  default <K extends ErrorKind> K kind() {
    throw new IllegalStateException("Type " + getClass() + " does not have an error kind");
  }

  default boolean isUnclassified() {
    return false;
  }

  public static Result<Nothing> success() {
    return new Success<>(Nothing.INSTANCE);
  }

  public static <T> Result<T> success(T value) {
    return new Success<>(value);
  }

  public static <T> Result<T> skip(String reason) {
    return new Skip<>(reason);
  }

  public static <T> Result<T> failure(ErrorKind kind, Throwable cause, String errorMessage) {
    return new Failure<>(kind, cause, errorMessage);
  }

  public static <T> Result<T> failure(Throwable cause, String errorMessage) {
    return failure(ErrorKind.unclassified(), cause, errorMessage);
  }

  public static <T> Result<T> failure(ErrorKind kind, String errorMessage) {
    return failure(kind, null, errorMessage);
  }

  public static <T> Result<T> failure(String errorMessage) {
    return failure(ErrorKind.unclassified(), null, errorMessage);
  }

  /**
   * Builds a failure with a formatted message. If the last argument is a {@link Throwable} it is also
   * used as the failure cause.
   */
  public static <T> Result<T> failure(String fmt, Object... args) {
    Throwable cause = null;
    if (args != null && args.length > 0 && args[args.length - 1] instanceof Throwable t) {
      cause = t;
    }
    return failure(cause, fmt, args);
  }

  public static <T> Result<T> failure(Throwable cause, String fmt, Object... args) {
    String errorMessage = String.format(fmt, args);
    return failure(cause, errorMessage);
  }

  public static <T> Result<T> failure(Throwable cause) {
    String errorMessage = cause == null || cause.getMessage() == null ? "generic error" : cause.getMessage();
    return failure(cause, errorMessage);
  }

  /**
   * Copies an unsuccessful result into a result of a different type, preserving explanation, cause
   * and (for single failures) the error kind.
   *
   * @throws IllegalArgumentException if {@code source} is successful
   */
  public static <T> Result<T> copyUnsuccessful(Result<?> source) {
    Result<?> src = Objects.requireNonNull(source, "source must not be null");
    if (src instanceof Failure<?> f) {
      return Result.failure(f.kind(), f.cause().orElse(null), f.explanation());
    }
    if (src instanceof Skip<?> s) {
      return Result.skip(s.explanation());
    }
    if (src instanceof CompositeResult<?> c && c.isUnsuccessful()) {
      if (c.results().stream().anyMatch(Result::isFailed)) {
        return Result.failure(ErrorKind.unclassified(), c.cause().orElse(null), c.explanation());
      }
      return Result.skip(c.explanation());
    }
    throw new IllegalArgumentException("Not an unsuccessful result " + source);
  }

  /**
   * @deprecated misspelled, use {@link #copyUnsuccessful(Result)}
   */
  @Deprecated
  public static <T> Result<T> copyUnsuccesful(Result<?> source) {
    return copyUnsuccessful(source);
  }
}
