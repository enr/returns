package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.testsupport.RandomStrings;

class ResultTest {

  @Test
  void errorResult() {
    String fallback = "fallback-" + RandomStrings.withLength(6);
    String errorMessage = "error-" + RandomStrings.withLength(6);
    Result<String> sut = Result.failingWithMessage(errorMessage);
    assertThat(sut.isSuccessful()).as("is success").isFalse();
    assertThat(sut.isFailed()).as("is error").isTrue();
    assertThat(sut.explanation()).as("error").isEqualTo(errorMessage);
    assertThat(sut.orElse(fallback)).as("fallback").isEqualTo(fallback);
  }

  @Test
  void successResult() {
    String payload = "payload-" + RandomStrings.withLength(6);
    Result<String> sut = Result.success(payload);
    assertThat(sut.isSuccessful()).as("is success").isTrue();
    assertThat(sut.isFailed()).as("is error").isFalse();
    assertThat(sut.explanation()).as("error message").isNull();
    assertThat(sut.cause()).as("error cause").isEmpty();
    assertThat(sut.unwrap()).as("error").isEqualTo(payload);
  }

}
