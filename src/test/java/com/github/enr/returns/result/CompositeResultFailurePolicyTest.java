package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.result.multi.CompositeResultFailurePolicy;

class CompositeResultFailurePolicyTest {

  @Test
  void testPolicyAllWhenNoResult() {
    List<Result<String>> results = List.of();
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.all();
    boolean result = sut.apply(results);
    assertThat(result).as("result").isFalse();
  }

  @Test
  void testAtLeastOne() {
    List<Result<String>> empty = List.of();
    List<Result<String>> oneFailure = List.of(Result.failure("err 1"), Result.failure("err 2"), Result.skip("skip"));
    List<Result<String>> noError = List.of(Result.success("ok"), Result.skip("skip"));
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.atLeastOne();
    assertThat(sut.apply(empty)).as("apply on empty").isFalse();
    assertThat(sut.apply(noError)).as("apply on no error").isFalse();
    assertThat(sut.apply(oneFailure)).as("apply on error").isTrue();
  }
}
