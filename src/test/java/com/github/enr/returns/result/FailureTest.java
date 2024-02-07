package com.github.enr.returns.result;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.testsupport.RandomStrings;
import com.github.enr.returns.testsupport.ResultAssertions;

class FailureTest {
  @Test
  void test01() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String errorMessage = "error-" + RandomStrings.withLength(10);
    Result<String> result = Result.failingWithMessage(errorMessage);
    ResultAssertions.verifyFailure(result, errorMessage, fallback);
  }
}
