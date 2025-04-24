package com.github.enr.returns.result;

public interface ErrorKind {

  public static ErrorKind unclassified() {
    return UnclassifiedError.INSTANCE;
  }
}
