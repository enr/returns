package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.result.multi.CompositeResultSuccessPolicy;

class CompositeResultSuccessPolicyTest {

  @Test
  void testPolicyAllWhenNoResult() {
    List<Result<String>> results = List.of();
    CompositeResultSuccessPolicy<String> sut = CompositeResultSuccessPolicy.all();
    boolean result = sut.apply(results);
    assertThat(result).as("result").isFalse();
  }

  @Test
  void testAtLeastOne() {
    List<Result<String>> empty = List.of();
    List<Result<String>> noSuccess = List.of(Result.failure("err 1"), Result.failure("err 2"), Result.skip("skip"));
    List<Result<String>> success = List.of(Result.success("ok"), Result.failure("err"));
    CompositeResultSuccessPolicy<String> sut = CompositeResultSuccessPolicy.atLeastOne();
    assertThat(sut.apply(empty)).as("apply on empty").isFalse();
    assertThat(sut.apply(noSuccess)).as("apply on no success").isFalse();
    assertThat(sut.apply(success)).as("apply on success").isTrue();
  }
}
