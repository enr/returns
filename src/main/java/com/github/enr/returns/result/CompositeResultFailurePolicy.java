package com.github.enr.returns.result;

import java.util.Collection;

@FunctionalInterface
public interface CompositeResultFailurePolicy<T> {

  public static <T> CompositeResultFailurePolicy<T> all() {
    return r -> !r.isEmpty() && r.stream().allMatch(Result::isSuccessful);
  }

  public static <T> CompositeResultFailurePolicy<T> atLeastOne() {
    return r -> !r.isEmpty() && r.stream().anyMatch(Result::isSuccessful);
  }

  boolean apply(Collection<Result<T>> results);
}
