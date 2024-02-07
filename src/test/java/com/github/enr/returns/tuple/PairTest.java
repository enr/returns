package com.github.enr.returns.tuple;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.testsupport.RandomStrings;

class PairTest {
  @Test
  void test01() {
    String first = "1-" + RandomStrings.withLength(12);
    String second = "2-" + RandomStrings.withLength(12);
    Pair<String, String> sut = Pair.of(first, second);
    assertThat(sut.first()).as("first").isEqualTo(first);
    assertThat(sut.second()).as("second").isEqualTo(second);
  }
}
