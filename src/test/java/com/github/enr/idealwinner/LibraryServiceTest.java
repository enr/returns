package com.github.enr.idealwinner;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LibraryServiceTest {

  @Test
  void test() {
    LibraryService sut = new LibraryService();
    assertThat(sut.operate("hello")).as("operate hello").isEqualTo("hello!");
  }
}
