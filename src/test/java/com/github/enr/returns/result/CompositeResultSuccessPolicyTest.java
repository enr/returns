package com.github.enr.returns.result;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.enr.returns.result.multi.CompositeResultSuccessPolicy;

class CompositeResultSuccessPolicyTest {

  @Test
  void testPolicyAllWhenNoResult() {
    List<Result<String>> results = List.of();
    CompositeResultSuccessPolicy<String> sut = CompositeResultSuccessPolicy.all();
    assertThat(sut.apply(results)).as("empty").isFalse();
  }

  @Test
  void testPolicyAllWhenAllSuccess() {
    List<Result<String>> results = List.of(Result.success("ok1"), Result.success("ok2"));
    assertThat(CompositeResultSuccessPolicy.<String>all().apply(results)).as("all success").isTrue();
  }

  @Test
  void testPolicyAllWhenMixedResults() {
    List<Result<String>> results = List.of(Result.success("ok"), Result.failure("err"));
    assertThat(CompositeResultSuccessPolicy.<String>all().apply(results)).as("mixed").isFalse();
  }

  @Test
  void testPolicyAllWhenAllFailure() {
    List<Result<String>> results = List.of(Result.failure("err1"), Result.failure("err2"));
    assertThat(CompositeResultSuccessPolicy.<String>all().apply(results)).as("all failure").isFalse();
  }

  @Test
  void testNoError() {
    CompositeResultSuccessPolicy<String> sut = CompositeResultSuccessPolicy.noError();
    assertThat(sut.apply(List.of())).as("empty").isFalse();
    assertThat(sut.apply(List.of(Result.success("ok")))).as("all success").isTrue();
    assertThat(sut.apply(List.of(Result.skip("skip")))).as("only skips").isTrue();
    assertThat(sut.apply(List.of(Result.failure("err")))).as("one failure").isFalse();
    assertThat(sut.apply(List.of(Result.success("ok"), Result.failure("err")))).as("mixed").isFalse();
  }

  @Test
  void testAtLeastOne() {
    List<Result<String>> empty = List.of();
    List<Result<String>> noSuccess = List.of(Result.failure("err 1"), Result.failure("err 2"), Result.skip("skip"));
    List<Result<String>> success = List.of(Result.success("ok"), Result.failure("err"));
    CompositeResultSuccessPolicy<String> sut = CompositeResultSuccessPolicy.atLeastOne();
    assertThat(sut.apply(empty)).as("apply on empty").isFalse();
    assertThat(sut.apply(noSuccess)).as("apply on no success").isFalse();
    assertThat(sut.apply(success)).as("apply on success").isTrue();
  }
}
