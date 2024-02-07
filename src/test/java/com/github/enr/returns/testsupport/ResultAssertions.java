package com.github.enr.returns.testsupport;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;

import com.github.enr.returns.result.Failure;
import com.github.enr.returns.result.Result;
import com.github.enr.returns.result.Success;

public class ResultAssertions {

  private ResultAssertions() {
    // noop
  }

  public static <T> void verifySuccess(Result<T> result, T value) {
    assertThat(result).as("result").isInstanceOf(Success.class);
    assertThat(result.isSuccessful()).as("is successful").isTrue();
    assertThat(result.isSkipped()).as("is skipped").isFalse();
    assertThat(result.isFailed()).as("is failed").isFalse();
    assertThat(result.isComposite()).as("is composite").isFalse();
    assertThat(result.unwrap()).as("unwrapped value").isEqualTo(value);
    assertThat(result.toOptional()).as("optional").contains(value);
    assertThat(result.explanation()).as("explanation").isNull();
    assertThat(result.cause()).as("cause").isEmpty();
  }

  public static <T> void verifyFailure(Result<T> result, String errorMessage, T fallback) {
    assertThat(result).as("result").isInstanceOf(Failure.class);
    assertThat(result.isSuccessful()).as("is successful").isFalse();
    assertThat(result.isSkipped()).as("is skipped").isFalse();
    assertThat(result.isFailed()).as("is failed").isTrue();
    assertThat(result.isComposite()).as("is composite").isFalse();
    Assertions.assertThrows(NoSuchElementException.class, () -> result.unwrap());
    assertThat(result.toOptional()).as("optional").isEmpty();
    assertThat(result.orElse(fallback)).as("or else").isEqualTo(fallback);
    assertThat(result.explanation()).as("explanation").isEqualTo(errorMessage);
    assertThat(result.cause()).as("cause").isEmpty();
  }

  public static <T> void verifySkip(Result<T> result, T fallback) {}
}
