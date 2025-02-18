package com.github.enr.returns.result.multi;

import java.util.Collection;

import com.github.enr.returns.result.Result;

/**
 * A functional interface that defines a policy for handling composite results.
 *
 * @param <T> the type of the result
 */
@FunctionalInterface
public interface CompositeResultFailurePolicy<T> {

  /**
   * Returns a policy that considers the composite result as failed if all individual results are
   * failed.
   *
   * @param <T> the type of the result
   * @return a policy that checks if all results are failed
   */
  public static <T> CompositeResultFailurePolicy<T> all() {
    return r -> !r.isEmpty() && r.stream().allMatch(Result::isFailed);
  }

  /**
   * Returns a policy that considers the composite result as failed if at least one individual result
   * is failed.
   *
   * @param <T> the type of the result
   * @return a policy that checks if at least one result is failed
   */
  public static <T> CompositeResultFailurePolicy<T> atLeastOne() {
    return r -> !r.isEmpty() && r.stream().anyMatch(Result::isFailed);
  }

  /**
   * Applies the policy to a collection of results.
   *
   * @param results the collection of results to apply the policy to
   * @return {@code true} if the policy condition is met, {@code false} otherwise
   */
  boolean apply(Collection<Result<T>> results);
}
