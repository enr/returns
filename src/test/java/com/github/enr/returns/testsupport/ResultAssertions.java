package com.github.enr.returns.testsupport;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;

import com.github.enr.returns.result.Result;
import com.github.enr.returns.result.uni.Failure;
import com.github.enr.returns.result.uni.Skip;
import com.github.enr.returns.result.uni.Success;

public class ResultAssertions {

  private ResultAssertions() {
    // noop
  }

  public static <T> void verifySuccess(Result<T> result, T value) {
    assertThat(result).as("result").isInstanceOf(Success.class);
    assertThat(result.isSuccessful()).as("is successful").isTrue();
    assertThat(result.isUnsuccessful()).as("is unsuccessful").isFalse();
    assertThat(result.isSkipped()).as("is skipped").isFalse();
    assertThat(result.isFailed()).as("is failed").isFalse();
    assertThat(result.isComposite()).as("is composite").isFalse();
    assertThat(result.unwrap()).as("unwrapped value").isEqualTo(value);
    assertThat(result.toOptional()).as("optional").contains(value);
    assertThat(result.explanation()).as("explanation").isNull();
    assertThat(result.cause()).as("cause").isEmpty();
  }

  public static <T> void verifyFailureFromMessage(Result<T> result, String errorMessage, T fallback) {
    verifyFailure(result, fallback);
    assertThat(result.explanation()).as("explanation").isEqualTo(errorMessage);
    assertThat(result.cause()).as("cause").isEmpty();
  }

  public static <T> void verifyFailureFromThrowable(Result<T> result, Throwable cause, T fallback) {
    verifyFailure(result, fallback);
    assertThat(result.explanation()).as("explanation").isEqualTo(cause.getMessage());
    assertThat(result.cause()).as("cause").contains(cause);
  }

  public static <T> void verifyFailure(Result<T> result, T fallback) {
    assertThat(result).as("result").isInstanceOf(Failure.class);
    assertThat(result.isSuccessful()).as("is successful").isFalse();
    assertThat(result.isUnsuccessful()).as("is unsuccessful").isTrue();
    assertThat(result.isSkipped()).as("is skipped").isFalse();
    assertThat(result.isFailed()).as("is failed").isTrue();
    assertThat(result.isComposite()).as("is composite").isFalse();
    Assertions.assertThrows(NoSuchElementException.class, () -> result.unwrap());
    assertThat(result.toOptional()).as("optional").isEmpty();
    assertThat(result.orElse(fallback)).as("or else").isEqualTo(fallback);
  }

  public static <T> void verifySkip(Result<T> result, String reason, T fallback) {
    assertThat(result).as("result").isInstanceOf(Skip.class);
    assertThat(result.isSuccessful()).as("is successful").isFalse();
    assertThat(result.isUnsuccessful()).as("is unsuccessful").isTrue();
    assertThat(result.isSkipped()).as("is skipped").isTrue();
    assertThat(result.isFailed()).as("is failed").isFalse();
    assertThat(result.isComposite()).as("is composite").isFalse();
    Assertions.assertThrows(NoSuchElementException.class, () -> result.unwrap());
    assertThat(result.toOptional()).as("optional").isEmpty();
    assertThat(result.orElse(fallback)).as("or else").isEqualTo(fallback);
    assertThat(result.explanation()).as("explanation").isEqualTo(reason);
    assertThat(result.cause()).as("cause").isEmpty();
  }
}
