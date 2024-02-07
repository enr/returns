package com.github.enr.returns.tuple;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.enr.returns.testsupport.RandomStrings;

class EitherTest {

  @Test
  void testLeft() {
    String value = "value-" + RandomStrings.withLength(12);
    Either<String, String> either = Either.left(value);
    assertThat(either.isLeft()).as("either is left").isTrue();
    assertThat(either.left()).as("either left").isEqualTo(value);
    assertThat(either.isRight()).as("either is right").isFalse();
    Assertions.assertThrows(NoSuchElementException.class, () -> either.right());
  }

  @Test
  void testRight() {
    String value = "value-" + RandomStrings.withLength(12);
    Either<String, String> either = Either.right(value);
    assertThat(either.isLeft()).as("either is left").isFalse();
    Assertions.assertThrows(NoSuchElementException.class, () -> either.left());
    assertThat(either.right()).as("either right").isEqualTo(value);
    assertThat(either.isRight()).as("either is right").isTrue();
  }

}
