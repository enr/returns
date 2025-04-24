package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Predicate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.enr.returns.result.uni.Failure;
import com.github.enr.returns.testsupport.RandomStrings;

class ResultTest {

  @Test
  void testIsErrorAnd() {
    String errorMessage = "error-" + RandomStrings.withLength(6);
    Predicate<Failure<String>> predicate = f -> errorMessage.equals(f.explanation());
    Result<String> failure = Result.failure(new Exception(), errorMessage);
    Result<String> skip = Result.skip(errorMessage);
    Result<String> success = Result.success(errorMessage);
    assertThat(failure.isErrorAnd(predicate)).as("failure").isTrue();
    assertThat(skip.isErrorAnd(predicate)).as("skip").isFalse();
    assertThat(success.isErrorAnd(predicate)).as("success").isFalse();
  }

  @Test
  void testCopyUnsuccessfulOnfailure() {
    String errorMessage = "error-" + RandomStrings.withLength(6);
    Result<String> source = Result.failure(new Exception(), errorMessage);
    Result<String> sut = Result.copyUnsuccesful(source);
    assertThat(sut.explanation()).as("error").isEqualTo(source.explanation());
    assertThat(sut.cause()).as("cause").isEqualTo(source.cause());
  }

  @Test
  void testCopyUnsuccessfulOnSkip() {
    String message = "error-" + RandomStrings.withLength(6);
    Result<String> source = Result.skip(message);
    Result<String> sut = Result.copyUnsuccesful(source);
    assertThat(sut.explanation()).as("explanation").isEqualTo(source.explanation());
    assertThat(sut.cause()).as("cause").isEqualTo(source.cause());
  }

  @Test
  void testCopyUnsuccessfulOnSuccess() {
    Result<Nothing> source = Result.success();
    Assertions.assertThrows(IllegalArgumentException.class, () -> Result.copyUnsuccesful(source));
  }

  @Test
  void errorResult() {
    String fallback = "fallback-" + RandomStrings.withLength(6);
    String errorMessage = "error-" + RandomStrings.withLength(6);
    Result<String> sut = Result.failure(errorMessage);
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
    Result<String> failure = Result.failure(errorMessage);
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

  @Test
  void successKind() {
    Result<String> result = Result.success("test");
    assertThat(result.is(UnclassifiedError.class)).as("is generic error kind").isFalse();
    assertThat(result.isFailureOfKind(UnclassifiedError.class)).as("is generic error kind").isFalse();
    Assertions.assertThrows(IllegalStateException.class, () -> result.kind());
  }

}
