package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class CompositeResultSuccessPolicyTest {

  @Test
  void testPolicyAllWhenNoResult() {
    List<Result<String>> results = List.of();
    CompositeResultSuccessPolicy<String> sut = CompositeResultSuccessPolicy.all();
    boolean result = sut.apply(results);
    assertThat(result).as("result").isFalse();
  }
}
