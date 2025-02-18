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
    assertThat(result).as("all policy with no results should be false").isFalse();
  }

  @Test
  void testPolicyAllWhenAllSuccess() {
    List<Result<String>> results = List.of(Result.success("ok1"), Result.success("ok2"));
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.all();
    boolean result = sut.apply(results);
    assertThat(result).as("all policy with all successes should be false").isFalse();
  }

  @Test
  void testPolicyAllWhenAllFailures() {
    List<Result<String>> results = List.of(Result.failure("err1"), Result.failure("err2"));
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.all();
    boolean result = sut.apply(results);
    assertThat(result).as("all policy with all failures should be true").isTrue();
  }

  @Test
  void testPolicyAllWhenMixedResults() {
    List<Result<String>> results = List.of(Result.success("ok"), Result.failure("err"), Result.skip("skip"));
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.all();
    boolean result = sut.apply(results);
    assertThat(result).as("all policy with mixed results should be false").isFalse();
  }

  @Test
  void testPolicyAllWhenOnlySkips() {
    List<Result<String>> results = List.of(Result.skip("skip1"), Result.skip("skip2"));
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.all();
    boolean result = sut.apply(results);
    assertThat(result).as("all policy with only skips should be false").isFalse();
  }

  @Test
  void testAtLeastOneWhenNoResult() {
    List<Result<String>> results = List.of();
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.atLeastOne();
    boolean result = sut.apply(results);
    assertThat(result).as("at least one policy with no results should be false").isFalse();
  }

  @Test
  void testAtLeastOneWhenAllSuccess() {
    List<Result<String>> results = List.of(Result.success("ok1"), Result.success("ok2"));
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.atLeastOne();
    boolean result = sut.apply(results);
    assertThat(result).as("at least one policy with all successes should be false").isFalse();
  }

  @Test
  void testAtLeastOneWhenAllFailures() {
    List<Result<String>> results = List.of(Result.failure("err1"), Result.failure("err2"));
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.atLeastOne();
    boolean result = sut.apply(results);
    assertThat(result).as("at least one policy with all failures should be true").isTrue();
  }

  @Test
  void testAtLeastOneWhenMixedResults() {
    List<Result<String>> results = List.of(Result.success("ok"), Result.failure("err"), Result.skip("skip"));
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.atLeastOne();
    boolean result = sut.apply(results);
    assertThat(result).as("at least one policy with mixed results should be true").isTrue();
  }

  @Test
  void testAtLeastOneWhenOnlySkips() {
    List<Result<String>> results = List.of(Result.skip("skip1"), Result.skip("skip2"));
    CompositeResultFailurePolicy<String> sut = CompositeResultFailurePolicy.atLeastOne();
    boolean result = sut.apply(results);
    assertThat(result).as("at least one policy with only skips should be false").isFalse();
  }

}
