package com.github.enr.returns.result.multi;

import java.util.Collection;

import com.github.enr.returns.result.Result;

@FunctionalInterface
public interface CompositeResultSkipPolicy<T> {

  public static <T> CompositeResultSkipPolicy<T> all() {
    return r -> !r.isEmpty() && r.stream().allMatch(Result::isSkipped);
  }

  public static <T> CompositeResultSkipPolicy<T> atLeastOne() {
    return r -> !r.isEmpty() && r.stream().anyMatch(Result::isSkipped);
  }

  boolean apply(Collection<Result<T>> results);
}
