package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ResultEqualityTest {

  enum TestKind implements ErrorKind {
    A, B
  }

  @Test
  void successEquality() {
    assertThat(Result.success("x")).isEqualTo(Result.success("x")).hasSameHashCodeAs(Result.success("x"));
    assertThat(Result.success("x")).isNotEqualTo(Result.success("y"));
    assertThat(Result.success("x")).isNotEqualTo(Result.skip("x"));
    assertThat(Result.success()).isEqualTo(Result.success());
  }

  @Test
  void skipEquality() {
    assertThat(Result.skip("r")).isEqualTo(Result.skip("r")).hasSameHashCodeAs(Result.skip("r"));
    assertThat(Result.skip(null)).isEqualTo(Result.skip(null));
    assertThat(Result.skip("r")).isNotEqualTo(Result.skip("s"));
    assertThat(Result.skip("r")).isNotEqualTo(Result.failure("r"));
  }

  @Test
  void failureEquality() {
    Exception cause = new Exception();
    assertThat(Result.failure(TestKind.A, cause, "m")).isEqualTo(Result.failure(TestKind.A, cause, "m"))
        .hasSameHashCodeAs(Result.failure(TestKind.A, cause, "m"));
    assertThat(Result.failure("m")).isEqualTo(Result.failure("m"));
    assertThat(Result.failure(TestKind.A, "m")).isNotEqualTo(Result.failure(TestKind.B, "m"));
    assertThat(Result.failure(TestKind.A, "m")).isNotEqualTo(Result.failure(TestKind.A, "n"));
    assertThat(Result.failure(TestKind.A, cause, "m")).isNotEqualTo(Result.failure(TestKind.A, new Exception(), "m"));
    assertThat(Result.failure("m")).isNotEqualTo(Result.skip("m"));
  }
}
