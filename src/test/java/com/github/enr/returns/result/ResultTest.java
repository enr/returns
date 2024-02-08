package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
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
    assertThat(sut.unwrap()).as("value").isEqualTo(payload);
    assertThat(sut.toOptional()).as("optional value").contains(payload);
  }

  @Test
  void successOrMethods() {
    String errorMessage = "error-" + RandomStrings.withLength(6);
    String fallback = "fallback-" + RandomStrings.withLength(6);
    String payload = "payload-" + RandomStrings.withLength(6);
    Result<String> success = Result.success(payload);
    String orFail = success.orFail(errorMessage);
    String orElse = success.orElse(fallback);
    assertThat(success.unwrap()).as("value").isEqualTo(payload);
    assertThat(orFail).as("orFail").isEqualTo(payload);
    assertThat(orElse).as("orElse").isEqualTo(payload);
  }

  @Test
  void failureOrMethods() {
    String orFailMessage = "orfail-" + RandomStrings.withLength(6);
    String errorMessage = "error-" + RandomStrings.withLength(6);
    String fallback = "fallback-" + RandomStrings.withLength(6);
    Result<String> failure = Result.failingWithMessage(errorMessage);
    Exception exception = Assertions.assertThrows(IllegalStateException.class, () -> failure.orFail(orFailMessage));
    assertThat(exception).as("exception").hasMessage(orFailMessage);
    String orElse = failure.orElse(fallback);
    assertThat(orElse).as("orElse").isEqualTo(fallback);
  }

  @Test
  void skipOrMethods() {
    String orFailMessage = "orfail-" + RandomStrings.withLength(6);
    String reason = "reason-" + RandomStrings.withLength(6);
    String fallback = "fallback-" + RandomStrings.withLength(6);
    Result<String> failure = Result.skip(reason);
    Exception exception = Assertions.assertThrows(IllegalStateException.class, () -> failure.orFail(orFailMessage));
    assertThat(exception).as("exception").hasMessage(orFailMessage);
    String orElse = failure.orElse(fallback);
    assertThat(orElse).as("orElse").isEqualTo(fallback);
  }

}
