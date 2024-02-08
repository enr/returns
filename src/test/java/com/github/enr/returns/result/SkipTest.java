package com.github.enr.returns.result;

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
}
