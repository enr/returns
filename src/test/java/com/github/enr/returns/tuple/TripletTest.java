package com.github.enr.returns.tuple;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.testsupport.RandomStrings;

class TripletTest {

  @Test
  void test01() {
    String first = "1-" + RandomStrings.withLength(12);
    String second = "2-" + RandomStrings.withLength(12);
    String third = "3-" + RandomStrings.withLength(12);
    Triplet<String, String, String> sut = Triplet.of(first, second, third);
    assertThat(sut.first()).as("first").isEqualTo(first);
    assertThat(sut.second()).as("second").isEqualTo(second);
    assertThat(sut.third()).as("third").isEqualTo(third);
  }

  @Test
  void equality() {
    assertThat(Triplet.of(1, "a", 2L)).isEqualTo(Triplet.of(1, "a", 2L)).hasSameHashCodeAs(Triplet.of(1, "a", 2L));
    assertThat(Triplet.of(1, "a", 2L)).isNotEqualTo(Triplet.of(1, "a", 3L));
  }

  @Test
  void testToString() {
    assertThat(Triplet.of(1, "a", 2L)).hasToString("Triplet(1, a, 2)");
  }

}
