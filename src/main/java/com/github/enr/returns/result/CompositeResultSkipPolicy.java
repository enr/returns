package com.github.enr.returns.result;

import java.util.Collection;

@FunctionalInterface
public interface CompositeResultSkipPolicy<T> {

  public static <T> CompositeResultSkipPolicy<T> all() {
    return r -> !r.isEmpty() && r.stream().allMatch(Result::isSuccessful);
  }

  public static <T> CompositeResultSkipPolicy<T> atLeastOne() {
    return r -> !r.isEmpty() && r.stream().anyMatch(Result::isSuccessful);
  }

  boolean apply(Collection<Result<T>> results);
}
