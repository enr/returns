package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Predicate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.enr.returns.result.multi.CompositeResult;
import com.github.enr.returns.result.multi.CompositeResultPolicies;
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
    Result<String> sut = Result.copyUnsuccessful(source);
    assertThat(sut.explanation()).as("error").isEqualTo(source.explanation());
    assertThat(sut.cause()).as("cause").isEqualTo(source.cause());
  }

  @Test
  void testCopyUnsuccessfulOnSkip() {
    String message = "error-" + RandomStrings.withLength(6);
    Result<String> source = Result.skip(message);
    Result<String> sut = Result.copyUnsuccessful(source);
    assertThat(sut.explanation()).as("explanation").isEqualTo(source.explanation());
    assertThat(sut.cause()).as("cause").isEqualTo(source.cause());
  }

  @Test
  void testCopyUnsuccessfulOnSuccess() {
    Result<Nothing> source = Result.success();
    Assertions.assertThrows(IllegalArgumentException.class, () -> Result.copyUnsuccessful(source));
  }

  @Test
  void testCopyUnsuccessfulOnNull() {
    Assertions.assertThrows(NullPointerException.class, () -> Result.copyUnsuccessful(null));
  }

  @Test
  void failureFromNullThrowable() {
    Result<String> result = Result.failure((Throwable) null);
    assertThat(result.isFailed()).isTrue();
    assertThat(result.explanation()).as("null cause falls back to generic error").isEqualTo("generic error");
    assertThat(result.cause()).isEmpty();
  }

  @Test
  void failureFromThrowableWithNullMessage() {
    Throwable cause = new RuntimeException((String) null);
    Result<String> result = Result.failure(cause);
    assertThat(result.isFailed()).isTrue();
    assertThat(result.explanation()).as("null message falls back to generic error").isEqualTo("generic error");
    assertThat(result.cause()).contains(cause);
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

  enum TestKind implements ErrorKind {
    NOT_FOUND
  }

  @Test
  void copyUnsuccessfulPreservesErrorKind() {
    Result<String> source = Result.failure(TestKind.NOT_FOUND, "missing");
    Result<Integer> sut = Result.copyUnsuccessful(source);
    assertThat(sut.isFailed()).as("is failed").isTrue();
    assertThat(sut.is(TestKind.class)).as("kind preserved").isTrue();
    assertThat(sut.isUnclassified()).as("is unclassified").isFalse();
    assertThat((TestKind) sut.kind()).as("kind").isEqualTo(TestKind.NOT_FOUND);
  }

  @Test
  @SuppressWarnings("deprecation")
  void deprecatedCopyUnsuccesfulDelegates() {
    Result<String> source = Result.failure(TestKind.NOT_FOUND, "missing");
    Result<Integer> sut = Result.copyUnsuccesful(source);
    assertThat(sut.is(TestKind.class)).as("kind preserved").isTrue();
    assertThat(sut.explanation()).as("explanation").isEqualTo("missing");
  }

  @Test
  void copyUnsuccessfulOnFailedComposite() {
    Exception cause = new Exception("boom");
    CompositeResult<String> composite = CompositeResult.empty(CompositeResultPolicies.all());
    composite.add(Result.failure("err1"));
    composite.add(Result.failure(cause, "err2"));
    Result<Integer> sut = Result.copyUnsuccessful(composite);
    assertThat(sut.isFailed()).as("is failed").isTrue();
    assertThat(sut.explanation()).as("explanation").contains("err1").contains("err2");
    assertThat(sut.cause()).as("cause").contains(cause);
  }

  @Test
  void copyUnsuccessfulOnPartialComposite() {
    CompositeResult<String> composite = CompositeResult.empty(CompositeResultPolicies.all());
    composite.add(Result.success("ok"));
    composite.add(Result.failure("err"));
    Result<Integer> sut = Result.copyUnsuccessful(composite);
    assertThat(sut.isFailed()).as("is failed").isTrue();
    assertThat(sut.explanation()).as("explanation").contains("err");
  }

  @Test
  void copyUnsuccessfulOnSkippedComposite() {
    CompositeResult<String> composite = CompositeResult.empty(CompositeResultPolicies.all());
    composite.add(Result.skip("reason-1"));
    composite.add(Result.skip("reason-2"));
    Result<Integer> sut = Result.copyUnsuccessful(composite);
    assertThat(sut.isSkipped()).as("is skipped").isTrue();
    assertThat(sut.explanation()).as("explanation").contains("reason-1").contains("reason-2");
  }

  @Test
  void copyUnsuccessfulOnSuccessfulCompositeThrows() {
    CompositeResult<String> composite = CompositeResult.empty(CompositeResultPolicies.all());
    composite.add(Result.success("ok"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> Result.copyUnsuccessful(composite));
  }

  @Test
  void formattedFailureUsesTrailingThrowableAsCause() {
    Exception cause = new Exception("boom");
    Result<String> sut = Result.failure("error on %s: %s", "item", cause);
    assertThat(sut.explanation()).as("explanation").isEqualTo("error on item: java.lang.Exception: boom");
    assertThat(sut.cause()).as("cause").contains(cause);
  }

  @Test
  void formattedFailureWithoutThrowableHasNoCause() {
    Result<String> sut = Result.failure("error on %s", "item");
    assertThat(sut.explanation()).as("explanation").isEqualTo("error on item");
    assertThat(sut.cause()).as("cause").isEmpty();
    assertThat(sut.isUnclassified()).as("is unclassified").isTrue();
  }

  @Test
  void formattedFailureWithExplicitCause() {
    Exception cause = new Exception("boom");
    Result<String> sut = Result.failure(cause, "error on %s", "item");
    assertThat(sut.explanation()).as("explanation").isEqualTo("error on item");
    assertThat(sut.cause()).as("cause").contains(cause);
  }

  @Test
  void isErrorAndWithNullPredicateThrowsNpeOnFailure() {
    Result<String> failure = Result.failure(new Exception(), "err");
    Assertions.assertThrows(NullPointerException.class, () -> failure.isErrorAnd(null));
  }

}
