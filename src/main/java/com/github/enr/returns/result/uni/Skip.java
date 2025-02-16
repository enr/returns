package com.github.enr.returns.result.uni;

import com.github.enr.returns.result.Result;
import com.github.enr.testexclusions.Generated;

public final class Skip<T> implements Result<T> {

  private final String reason;

  public Skip(String reason) {
    this.reason = reason;
  }

  @Override
  public boolean isSkipped() {
    return true;
  }

  @Override
  public String explanation() {
    return reason;
  }

  @Generated
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Skip [");
    builder.append(reason);
    builder.append("]");
    return builder.toString();
  }

}
