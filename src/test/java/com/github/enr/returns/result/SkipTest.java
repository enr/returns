package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.testsupport.RandomStrings;
import com.github.enr.returns.testsupport.ResultAssertions;

class SkipTest {

  @Test
  void test() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String reason = "Test reason";
    Result<String> result = Result.skip(reason);
    ResultAssertions.verifySkip(result, reason, fallback);
  }

  @Test
  void skipWithNullReasonIsPermitted() {
    Result<String> result = Result.skip(null);
    assertThat(result.isSkipped()).as("is skipped").isTrue();
    assertThat(result.explanation()).as("null reason preserved as null explanation").isNull();
  }

  @Test
  void skipWithEmptyReasonIsPermitted() {
    Result<String> result = Result.skip("");
    assertThat(result.isSkipped()).as("is skipped").isTrue();
    assertThat(result.explanation()).as("empty reason").isEmpty();
  }
}
