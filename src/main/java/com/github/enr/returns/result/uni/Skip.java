package com.github.enr.returns.result.uni;

import com.github.enr.returns.result.Result;

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
}
