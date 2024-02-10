package com.github.enr.returns.result.multi;

import java.util.Collection;

import com.github.enr.returns.result.Result;

@FunctionalInterface
public interface CompositeResultFailurePolicy<T> {

  public static <T> CompositeResultFailurePolicy<T> all() {
    return r -> !r.isEmpty() && r.stream().allMatch(Result::isFailed);
  }

  public static <T> CompositeResultFailurePolicy<T> atLeastOne() {
    return r -> !r.isEmpty() && r.stream().anyMatch(Result::isFailed);
  }

  boolean apply(Collection<Result<T>> results);
}
