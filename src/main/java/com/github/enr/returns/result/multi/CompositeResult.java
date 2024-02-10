package com.github.enr.returns.result.multi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.enr.returns.result.Result;

public final class CompositeResult<T> implements Result<Collection<T>> {

  private static final String EOL = "\n";

  private final CompositeResultSuccessPolicy<T> successPolicy;
  private final CompositeResultFailurePolicy<T> failurePolicy;
  private final CompositeResultSkipPolicy<T> skipPolicy;

  private List<Result<T>> results = new ArrayList<>();

  private CompositeResult(CompositeResultPolicies<T> policies) {
    this.successPolicy = policies.success();
    this.skipPolicy = policies.skip();
    this.failurePolicy = policies.failure();
  }

  public void addAll(Collection<Result<T>> results) {
    for (Result<T> r : results) {
      add(r);
    }
  }

  public void add(Result<T> result) {
    results.add(result);
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

  @Override
  public String explanation() {
    Collection<Result<T>> fail = results.stream().filter(Result::isFailed).toList();
    if (fail.isEmpty()) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("Got ").append(fail.size()).append(" errors:");
    fail.stream().forEach(f -> sb.append(EOL).append("- ").append(f.explanation()));
    return sb.toString();
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
    return successPolicy.apply(results());
  }

  @Override
  public boolean isSkipped() {
    return skipPolicy.apply(results());
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
    return new CompositeResult<>(policies);
  }
}
