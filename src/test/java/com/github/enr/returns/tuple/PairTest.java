package com.github.enr.returns.tuple;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

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

  @Test
  void equality() {
    assertThat(Pair.of(1, "a")).isEqualTo(Pair.of(1, "a")).hasSameHashCodeAs(Pair.of(1, "a"));
    assertThat(Pair.of(1, "a")).isNotEqualTo(Pair.of(1, "b"));
    assertThat(Pair.of(null, null)).isEqualTo(Pair.of(null, null));
    assertThat(Set.of(Pair.of(1, "a"))).contains(Pair.of(1, "a"));
  }

  @Test
  void testToString() {
    assertThat(Pair.of(1, "a")).hasToString("Pair(1, a)");
  }
}
