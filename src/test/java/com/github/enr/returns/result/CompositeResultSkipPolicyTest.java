package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class CompositeResultSkipPolicyTest {

  @Test
  void testPolicyAllWhenNoResult() {
    List<Result<String>> results = List.of();
    CompositeResultSkipPolicy<String> sut = CompositeResultSkipPolicy.all();
    boolean result = sut.apply(results);
    assertThat(result).as("result").isFalse();
  }
}
