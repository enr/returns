package com.github.enr.returns.result.multi;

import java.util.Collection;

import com.github.enr.returns.result.Result;

/**
 * A functional interface that defines a policy for skipping composite results.
 *
 * @param <T> the type of the result
 */
@FunctionalInterface
public interface CompositeResultSkipPolicy<T> {

  /**
   * Returns a skip policy that skips if all results are skipped.
   *
   * @param <T> the type of the result
   * @return a skip policy that skips if all results are skipped
   */
  public static <T> CompositeResultSkipPolicy<T> all() {
    return r -> !r.isEmpty() && r.stream().allMatch(Result::isSkipped);
  }

  /**
   * Returns a skip policy that skips if at least one result is skipped.
   *
   * @param <T> the type of the result
   * @return a skip policy that skips if at least one result is skipped
   */
  public static <T> CompositeResultSkipPolicy<T> atLeastOne() {
    return r -> !r.isEmpty() && r.stream().anyMatch(Result::isSkipped);
  }

  /**
   * Applies the skip policy to a collection of results.
   *
   * @param results the collection of results
   * @return true if the policy condition is met, false otherwise
   */
  boolean apply(Collection<Result<T>> results);
}
