package com.github.enr.returns.testsupport;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.enr.returns.result.Result;
import com.github.enr.returns.result.multi.CompositeResult;

public class CompositeResultAssertions {

  public static <T> CompositeResult<T> verifyPartial(Result<T> result, T fallback) {
    return null;
  }

  public static <T> CompositeResult<T> verifySuccess(Result<T> result) {
    assertThat(result).as("result").isInstanceOf(CompositeResult.class);
    assertThat(result.isSuccessful()).as("is successful").isTrue();
    assertThat(result.isSkipped()).as("is skipped").isFalse();
    assertThat(result.isFailed()).as("is failed").isFalse();
    assertThat(result.isComposite()).as("is composite").isFalse();
    assertThat(result.explanation()).as("explanation").isNull();
    assertThat(result.cause()).as("cause").isEmpty();

    @SuppressWarnings("unchecked")
    CompositeResult<T> composite = (CompositeResult<T>) result;
    return composite;
  }

  public static <T> CompositeResult<T> verifySkip(Result<T> result, T fallback) {
    return null;
  }

  public static <T> CompositeResult<T> verifyFailure(Result<T> result, T fallback) {
    return null;
  }
}
