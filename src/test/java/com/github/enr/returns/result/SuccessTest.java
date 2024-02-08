package com.github.enr.returns.result;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.testsupport.RandomStrings;
import com.github.enr.returns.testsupport.ResultAssertions;

class SuccessTest {

  @Test
  void test() {
    String value = "value-" + RandomStrings.withLength(10);
    Result<String> result = Result.success(value);
    ResultAssertions.verifySuccess(result, value);
  }

  @Test
  void testEmpty() {
    Result<Nothing> result = Result.success();
    ResultAssertions.verifySuccess(result, Nothing.INSTANCE);
  }

}
