package com.github.enr.returns.result;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.testsupport.RandomStrings;
import com.github.enr.returns.testsupport.ResultAssertions;

class FailureTest {

  @Test
  void testErrorMessage() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String errorMessage = "error-" + RandomStrings.withLength(10);
    Result<String> result = Result.failingWithMessage(errorMessage);
    ResultAssertions.verifyFailure(result, errorMessage, fallback);
  }

  @Test
  void testErrorMessageWithArguments() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String errorMessageTemplate = "error-%s";
    String errorMessageArg = "test";
    String expectedErrorMessage = "error-test";
    Result<String> result = Result.failingWithMessage(errorMessageTemplate, errorMessageArg);
    ResultAssertions.verifyFailure(result, expectedErrorMessage, fallback);
  }

  @Test
  void testCause() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String errorMessage = "error-" + RandomStrings.withLength(10);
    Exception cause = new Exception(errorMessage);
    Result<String> result = Result.failingWithCause(cause);
    ResultAssertions.verifyFailure(result, cause, fallback);
  }
}
