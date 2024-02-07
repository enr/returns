package com.github.enr.returns.result;

import java.util.Collection;

@FunctionalInterface
public interface CompositeResultSuccessPolicy<T> {

  public static <T> CompositeResultSuccessPolicy<T> all() {
    return r -> !r.isEmpty() && r.stream().allMatch(Result::isSuccessful);
  }

  public static <T> CompositeResultSuccessPolicy<T> atLeastOne() {
    return r -> !r.isEmpty() && r.stream().anyMatch(Result::isSuccessful);
  }

  public static <T> CompositeResultSuccessPolicy<T> noError() {
    return r -> r.stream().noneMatch(Result::isFailed);
  }

  boolean apply(Collection<Result<T>> results);
}
