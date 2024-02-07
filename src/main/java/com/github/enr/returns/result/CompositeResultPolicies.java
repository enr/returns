package com.github.enr.returns.result;

public record CompositeResultPolicies<T>(CompositeResultSuccessPolicy<T> success, CompositeResultSkipPolicy<T> skip,
    CompositeResultFailurePolicy<T> failure) {

  public static <T> CompositeResultPolicies<T> all() {
    return new CompositeResultPolicies<>(CompositeResultSuccessPolicy.all(), CompositeResultSkipPolicy.all(),
        CompositeResultFailurePolicy.all());
  }

  public static <T> CompositeResultPolicies<T> atLeastOne() {
    return new CompositeResultPolicies<>(CompositeResultSuccessPolicy.atLeastOne(),
        CompositeResultSkipPolicy.atLeastOne(), CompositeResultFailurePolicy.atLeastOne());
  }
}
