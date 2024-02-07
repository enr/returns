package com.github.enr.returns.testsupport;

import java.util.concurrent.ThreadLocalRandom;

public class RandomStrings {

  private RandomStrings() {
    // only static methods
  }

  private static final char[] ALPHANUM = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

  public static String withLength(int length) {
    if (length < 0) {
      throw new IllegalArgumentException("Lenght must be greater than 0");
    }
    if (length == 0) {
      return "";
    }
    StringBuilder str = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      str.append(ALPHANUM[ThreadLocalRandom.current().nextInt(0, ALPHANUM.length)]);
    }
    return str.toString();
  }

}
