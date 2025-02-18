package com.github.enr.returns.result.multi;

import java.util.Collection;

import com.github.enr.returns.result.Result;

/**
 * A functional interface that defines policies for determining the success of a collection of
 * {@link Result} objects.
 *
 * @param <T> the type of the result value
 */
@FunctionalInterface
public interface CompositeResultSuccessPolicy<T> {

  /**
   * Returns a CompositeResultSuccessPolicy that checks if all results in a collection are successful.
   *
   * @param <T> the type of the result
   * @return a CompositeResultSuccessPolicy that returns true if the collection is not empty and all
   *         results are successful
   */
  public static <T> CompositeResultSuccessPolicy<T> all() {
    return r -> !r.isEmpty() && r.stream().allMatch(Result::isSuccessful);
  }

  /**
   * Returns a CompositeResultSuccessPolicy that checks if at least one result in a collection is
   * successful.
   *
   * @param <T> the type of the result
   * @return a CompositeResultSuccessPolicy that returns true if the collection is not empty and at
   *         least one result is successful
   */
  public static <T> CompositeResultSuccessPolicy<T> atLeastOne() {
    return r -> !r.isEmpty() && r.stream().anyMatch(Result::isSuccessful);
  }

  /**
   * Returns a CompositeResultSuccessPolicy that checks if no results in a collection have failed.
   *
   * @param <T> the type of the result
   * @return a CompositeResultSuccessPolicy that returns true if no results in the collection have
   *         failed
   */
  public static <T> CompositeResultSuccessPolicy<T> noError() {
    return r -> r.stream().noneMatch(Result::isFailed);
  }

  /**
   * Applies the policy to a collection of results.
   *
   * @param results the collection of results to check
   * @return true if the collection meets the policy criteria, false otherwise
   */
  boolean apply(Collection<Result<T>> results);
}
