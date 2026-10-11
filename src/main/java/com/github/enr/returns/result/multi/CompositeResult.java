package com.github.enr.returns.result.multi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.github.enr.returns.result.Result;

/**
 * A result made of many results.
 *
 * <p>
 * The states are mutually exclusive: failure takes precedence over success, and success takes
 * precedence over skip. So a composite matching both the failure and the success policy is only
 * failed.
 */
public final class CompositeResult<T> implements Result<Collection<T>> {

  private static final String EOL = "\n";

  private final CompositeResultSuccessPolicy<T> successPolicy;
  private final CompositeResultFailurePolicy<T> failurePolicy;
  private final CompositeResultSkipPolicy<T> skipPolicy;

  private final List<Result<T>> results = new ArrayList<>();

  private CompositeResult(CompositeResultPolicies<T> policies) {
    this.successPolicy = Objects.requireNonNull(policies.success(), "success policy must not be null");
    this.skipPolicy = Objects.requireNonNull(policies.skip(), "skip policy must not be null");
    this.failurePolicy = Objects.requireNonNull(policies.failure(), "failure policy must not be null");
  }

  public void addAll(Collection<Result<T>> results) {
    Objects.requireNonNull(results, "results must not be null");
    for (Result<T> r : results) {
      add(r);
    }
  }

  public void add(Result<T> result) {
    results.add(Objects.requireNonNull(result, "result must not be null"));
  }

  public Collection<Result<T>> results() {
    return List.copyOf(results);
  }

  /*
   * true if at least 1 success but not all success
   */
  public boolean isPartial() {
    return ((!success().isEmpty()) && (success().size() != results.size()));
  }

  /*
   * The failures explanations or, if no failure and the result is not successful, the skip reasons.
   */
  @Override
  public String explanation() {
    Collection<Result<T>> fail = results.stream().filter(Result::isFailed).toList();
    if (!fail.isEmpty()) {
      return describe(fail, "errors");
    }
    Collection<Result<T>> skip = results.stream().filter(Result::isSkipped).toList();
    if (!skip.isEmpty() && !isSuccessful()) {
      return describe(skip, "skipped");
    }
    return null;
  }

  private static <T> String describe(Collection<Result<T>> items, String label) {
    StringBuilder sb = new StringBuilder();
    sb.append("Got ").append(items.size()).append(' ').append(label).append(':');
    items.stream().forEach(f -> sb.append(EOL).append("- ").append(f.explanation()));
    return sb.toString();
  }

  /*
   * The cause of the first failed result having one.
   */
  @Override
  public Optional<Throwable> cause() {
    return results.stream().filter(Result::isFailed).map(Result::cause).flatMap(Optional::stream).findFirst();
  }

  @Override
  public boolean isComposite() {
    return true;
  }

  @Override
  public boolean isFailed() {
    return failurePolicy.apply(results());
  }

  @Override
  public boolean isSuccessful() {
    return !isFailed() && successPolicy.apply(results());
  }

  @Override
  public boolean isSkipped() {
    return !isFailed() && !isSuccessful() && skipPolicy.apply(results());
  }

  private Collection<Result<T>> success() {
    return results.stream().filter(Result::isSuccessful).toList();
  }

  /*
   * Values for the successful results
   */
  @Override
  public Collection<T> unwrap() {
    return success().stream().map(Result::unwrap).toList();
  }

  public static <T> CompositeResult<T> empty(CompositeResultPolicies<T> policies) {
    Objects.requireNonNull(policies, "policies must not be null");
    return new CompositeResult<>(policies);
  }
}
