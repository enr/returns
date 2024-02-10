package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.result.multi.CompositeResultSkipPolicy;

class CompositeResultSkipPolicyTest {

  @Test
  void testPolicyAllWhenNoResult() {
    List<Result<String>> results = List.of();
    CompositeResultSkipPolicy<String> sut = CompositeResultSkipPolicy.all();
    boolean result = sut.apply(results);
    assertThat(result).as("result").isFalse();
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
