package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.enr.returns.testsupport.RandomStrings;
import com.github.enr.returns.testsupport.ResultAssertions;

class FailureTest {

  @Test
  void testErrorMessage() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String errorMessage = "error-" + RandomStrings.withLength(10);
    Result<String> result = Result.failure(errorMessage);
    ResultAssertions.verifyFailureFromMessage(result, errorMessage, fallback);
    assertThat(result.isFailureOfKind(UnclassifiedError.class)).as("Response class is failure of kind unclassified")
        .isTrue();
    assertThat(result.is(UnclassifiedError.class)).as("Response class is unclassified").isTrue();
    assertThat(result.isUnclassified()).as("Response class is unclassified").isTrue();
    UnclassifiedError ge = result.kind();
    assertThat(ge).as("Response kind").isNotNull();
  }

  @Test
  void testErrorMessageWithArguments() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String errorMessageTemplate = "error-%s";
    String errorMessageArg = "test";
    String expectedErrorMessage = "error-test";
    Result<String> result = Result.failure(errorMessageTemplate, errorMessageArg);
    ResultAssertions.verifyFailureFromMessage(result, expectedErrorMessage, fallback);
  }

  @Test
  void testCause() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String errorMessage = "error-" + RandomStrings.withLength(10);
    Exception cause = new Exception(errorMessage);
    Result<String> result = Result.failure(cause);
    ResultAssertions.verifyFailureFromThrowable(result, cause, fallback);
  }

  private static class FooError implements ErrorKind {
  }
  private static class BarError implements ErrorKind {
  }

  @Test
  void testKindOnResult() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String errorMessage = "error-" + RandomStrings.withLength(10);
    Exception cause = new Exception(errorMessage);
    Result<String> result = Result.failure(new FooError(), cause, errorMessage);
    ResultAssertions.verifyFailureFromThrowable(result, cause, fallback);

    assertThat(result.isFailureOfKind(FooError.class)).as("Response class is failure of kind foo").isTrue();
    assertThat(result.is(FooError.class)).as("Response class is foo").isTrue();
    FooError ae = result.kind();
    assertThat(ae).as("Response is foo").isNotNull();
    assertThat(result.isUnclassified()).as("Response class is unclassified").isFalse();
    Assertions.assertThrows(ClassCastException.class, () -> {
      BarError be = result.kind();
    });
  }

  @Test
  void testErrorMessageWithKind() {
    String fallback = "fallback-" + RandomStrings.withLength(10);
    String errorMessage = "error-" + RandomStrings.withLength(10);
    Result<String> result = Result.failure(new FooError(), errorMessage);
    System.err.println("result: " + result.getClass());
    ResultAssertions.verifyFailureFromMessage(result, errorMessage, fallback);

    assertThat(result.isFailureOfKind(FooError.class)).as("Response class is failure of kind foo").isTrue();
    assertThat(result.is(FooError.class)).as("Response class is foo").isTrue();
    assertThat(result.is(BarError.class)).as("Response class is foo").isFalse();
    FooError ae = result.kind();
    assertThat(ae).as("Response is foo").isNotNull();
    assertThat(result.isUnclassified()).as("Response class is unclassified").isFalse();
    Assertions.assertThrows(ClassCastException.class, () -> {
      BarError be = result.kind();
    });
  }
}
