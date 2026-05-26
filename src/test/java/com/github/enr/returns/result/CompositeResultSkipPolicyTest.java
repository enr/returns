package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.result.multi.CompositeResultSkipPolicy;

class CompositeResultSkipPolicyTest {

  @Test
  void testPolicyAllWhenNoResult() {
    List<Result<String>> results = List.of();
    assertThat(CompositeResultSkipPolicy.<String>all().apply(results)).as("empty").isFalse();
  }

  @Test
  void testPolicyAllWhenAllSkips() {
    List<Result<String>> results = List.of(Result.skip("s1"), Result.skip("s2"));
    assertThat(CompositeResultSkipPolicy.<String>all().apply(results)).as("all skips").isTrue();
  }

  @Test
  void testPolicyAllWhenMixed() {
    List<Result<String>> results = List.of(Result.skip("s1"), Result.success("ok"));
    assertThat(CompositeResultSkipPolicy.<String>all().apply(results)).as("mixed").isFalse();
  }

  @Test
  void testAtLeastOne() {
    List<Result<String>> empty = List.of();
    List<Result<String>> oneSkip = List.of(Result.failure("err 1"), Result.failure("err 2"), Result.skip("skip"));
    List<Result<String>> noSkip = List.of(Result.success("ok"), Result.failure("err"));
    CompositeResultSkipPolicy<String> sut = CompositeResultSkipPolicy.atLeastOne();
    assertThat(sut.apply(empty)).as("apply on empty").isFalse();
    assertThat(sut.apply(noSkip)).as("apply on no skip").isFalse();
    assertThat(sut.apply(oneSkip)).as("apply on skip").isTrue();
  }
}
