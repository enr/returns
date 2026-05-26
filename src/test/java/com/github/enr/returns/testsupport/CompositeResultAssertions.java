package com.github.enr.returns.testsupport;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.github.enr.returns.result.Result;
import com.github.enr.returns.result.multi.CompositeResult;

public class CompositeResultAssertions {

  public static <T> CompositeResult<T> verifyPartial(Result<T> result, T fallback) {
    assertThat(result).as("result").isInstanceOf(CompositeResult.class);
    @SuppressWarnings("unchecked")
    CompositeResult<T> composite = (CompositeResult<T>) result;
    assertThat(composite.isPartial()).as("is partial").isTrue();
    assertThat(composite.isComposite()).as("is composite").isTrue();
    assertThat(composite.orElse(List.of(fallback))).as("or else fallback").containsExactly(fallback);
    return composite;
  }

  public static <T> CompositeResult<T> verifySuccess(Result<T> result) {
    assertThat(result).as("result").isInstanceOf(CompositeResult.class);
    assertThat(result.isSuccessful()).as("is successful").isTrue();
    assertThat(result.isSkipped()).as("is skipped").isFalse();
    assertThat(result.isFailed()).as("is failed").isFalse();
    assertThat(result.isComposite()).as("is composite").isTrue();
    assertThat(result.explanation()).as("explanation").isNull();
    assertThat(result.cause()).as("cause").isEmpty();

    @SuppressWarnings("unchecked")
    CompositeResult<T> composite = (CompositeResult<T>) result;
    return composite;
  }

  public static <T> CompositeResult<T> verifySkip(Result<T> result, T fallback) {
    assertThat(result).as("result").isInstanceOf(CompositeResult.class);
    @SuppressWarnings("unchecked")
    CompositeResult<T> composite = (CompositeResult<T>) result;
    assertThat(composite.isSkipped()).as("is skipped").isTrue();
    assertThat(composite.isSuccessful()).as("is successful").isFalse();
    assertThat(composite.isFailed()).as("is failed").isFalse();
    assertThat(composite.isComposite()).as("is composite").isTrue();
    assertThat(composite.orElse(List.of(fallback))).as("or else fallback").containsExactly(fallback);
    return composite;
  }

  public static <T> CompositeResult<T> verifyFailure(Result<T> result, T fallback) {
    assertThat(result).as("result").isInstanceOf(CompositeResult.class);
    @SuppressWarnings("unchecked")
    CompositeResult<T> composite = (CompositeResult<T>) result;
    assertThat(composite.isFailed()).as("is failed").isTrue();
    assertThat(composite.isSuccessful()).as("is successful").isFalse();
    assertThat(composite.isSkipped()).as("is skipped").isFalse();
    assertThat(composite.isComposite()).as("is composite").isTrue();
    assertThat(composite.explanation()).as("explanation").isNotNull();
    assertThat(composite.orElse(List.of(fallback))).as("or else fallback").containsExactly(fallback);
    return composite;
  }
}
