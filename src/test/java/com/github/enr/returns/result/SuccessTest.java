package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
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

  @Test
  void testIfSuccessConsumerIsCalled() {
    String value = "value-" + RandomStrings.withLength(10);
    Result<String> result = Result.success(value);
    List<String> captured = new ArrayList<>();
    result.ifSuccess(captured::add);
    assertThat(captured).containsExactly(value);
  }

  @Test
  void testIfSuccessNotCalledOnFailure() {
    Result<String> result = Result.failure("err");
    List<String> captured = new ArrayList<>();
    result.ifSuccess(captured::add);
    assertThat(captured).isEmpty();
  }

  @Test
  void testNullValueRejected() {
    Assertions.assertThrows(NullPointerException.class, () -> Result.success(null));
  }

}
