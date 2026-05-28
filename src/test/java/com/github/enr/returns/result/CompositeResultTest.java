package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.result.multi.CompositeResult;
import com.github.enr.returns.result.multi.CompositeResultPolicies;
import com.github.enr.returns.testsupport.CompositeResultAssertions;
import com.github.enr.returns.testsupport.RandomStrings;

class CompositeResultTest {

  @Test
  void test01() {
    String payload = "payload-" + RandomStrings.withLength(6);
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.success(payload));
    sut.add(Result.failure("oh no"));
    assertThat(sut).as("result").isInstanceOf(CompositeResult.class);
    assertThat(sut.isPartial()).as("is partial").isTrue();
    assertThat(sut.isSuccessful()).as("is successful").isFalse();
    assertThat(sut.isSkipped()).as("is skipped").isFalse();
    assertThat(sut.isFailed()).as("is failed").isFalse();
    assertThat(sut.isComposite()).as("is composite").isTrue();
    assertThat(sut.explanation()).as("explanation").contains("oh no");
    assertThat(sut.cause()).as("cause").isEmpty();
  }

  @Test
  void fullSuccess() {
    String payload = "payload-" + RandomStrings.withLength(6);
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.success(payload));
    assertThat(sut.isPartial()).as("is partial").isFalse();
    assertThat(sut.explanation()).as("error").isNull();
    assertThat(sut.unwrap()).as("success result").hasSize(1).contains(payload);
    assertThat(sut.orElse(List.of("fallback"))).as("fallback").containsExactly(payload);
  }

  @Test
  void partialResult() {
    String payload = "payload-" + RandomStrings.withLength(6);
    String fallback = "fallback-" + RandomStrings.withLength(6);
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.success(payload));
    sut.add(Result.failure("oh no"));
    assertThat(sut.isPartial()).as("is partial").isTrue();
    assertThat(sut.explanation()).as("error").contains("errors");
    assertThat(sut.unwrap()).as("success result").hasSize(1).contains(payload);
    assertThat(sut.orElse(List.of(fallback))).as("fallback").containsExactly(fallback);
  }

  @Test
  void multipleError() {
    String err1 = "error-" + RandomStrings.withLength(6);
    String err2 = "error-" + RandomStrings.withLength(6);
    String fallback = "fallback-" + RandomStrings.withLength(6);
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.addAll(List.of(Result.failure(err1), Result.failure(err2)));
    assertThat(sut.explanation()).as("error message").contains(err1).contains(err2);
    assertThat(sut.orElse(List.of(fallback))).as("fallback").containsExactly(fallback);
  }

  @Test
  void resultsReturnsImmutableSnapshot() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.success("a"));
    sut.add(Result.failure("err"));
    assertThat(sut.results()).hasSize(2);
  }

  @Test
  void atLeastOnePolicyBehavior() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.atLeastOne());
    sut.add(Result.success("ok"));
    sut.add(Result.failure("err"));
    assertThat(sut.isSuccessful()).as("atLeastOne: one success → successful").isTrue();
    assertThat(sut.isFailed()).as("atLeastOne: one failure → failed").isTrue();
    assertThat(sut.isSkipped()).as("atLeastOne: no skip → not skipped").isFalse();
  }

  @Test
  void allFailureIsNotPartial() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.addAll(List.of(Result.failure("err1"), Result.failure("err2")));
    assertThat(sut.isPartial()).as("all-failure is not partial").isFalse();
  }

  @Test
  void unwrapWithNoSuccessesReturnsEmptyCollection() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.addAll(List.of(Result.failure("err1"), Result.failure("err2")));
    assertThat(sut.unwrap()).as("unwrap with no successes").isEmpty();
  }

  @Test
  void resultsSnapshotIsUnmodifiable() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.success("a"));
    Collection<Result<String>> snapshot = sut.results();
    assertThatThrownBy(() -> snapshot.add(Result.success("b")))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void resultsSnapshotDoesNotReflectLaterAdditions() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.success("a"));
    Collection<Result<String>> snapshot = sut.results();
    sut.add(Result.failure("err"));
    assertThat(snapshot).as("snapshot taken before second add").hasSize(1);
  }

  @Test
  void emptyCompositeIsNeitherSuccessNorFailedNorSkipped() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    assertThat(sut.isSuccessful()).as("is successful").isFalse();
    assertThat(sut.isFailed()).as("is failed").isFalse();
    assertThat(sut.isSkipped()).as("is skipped").isFalse();
    assertThat(sut.isPartial()).as("is partial").isFalse();
    assertThat(sut.explanation()).as("explanation").isNull();
    assertThat(sut.unwrap()).as("unwrap").isEmpty();
  }

  @Test
  void orFailOnNonSuccessfulCompositeThrows() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.failure("err"));
    assertThatThrownBy(() -> sut.orFail("composite failed"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("composite failed");
  }

  @Test
  void allSuccessCompositeReportsSuccess() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.success("ok1"));
    sut.add(Result.success("ok2"));
    CompositeResultAssertions.verifySuccess(sut);
  }

  @Test
  void allFailureCompositeReportsFailure() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.failure("err1"));
    sut.add(Result.failure("err2"));
    CompositeResultAssertions.verifyFailure(sut, List.of("fallback"));
  }

  @Test
  void allSkipsCompositeReportsSkip() {
    CompositeResult<String> sut = CompositeResult.empty(CompositeResultPolicies.all());
    sut.add(Result.skip("reason-1"));
    sut.add(Result.skip("reason-2"));
    CompositeResultAssertions.verifySkip(sut, List.of("fallback"));
  }

}
