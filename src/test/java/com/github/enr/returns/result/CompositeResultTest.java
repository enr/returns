package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.result.multi.CompositeResult;
import com.github.enr.returns.result.multi.CompositeResultPolicies;
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

}
