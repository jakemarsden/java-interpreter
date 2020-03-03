package com.jakemarsden.java.lexer.text;

import static java.util.Objects.requireNonNull;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface CharIterator extends PrimitiveIterator<Character, CharConsumer> {

  /**
   * @return {@code true} if there are {@code >=1} characters remaining, and it's safe to call
   *     {@link #next()}
   */
  @Override
  boolean hasNext();

  char nextChar();

  /**
   * @return the next character
   * @throws NoSuchElementException if there are no further characters
   */
  @Override
  default Character next() {
    return this.nextChar();
  }

  @Override
  default void forEachRemaining(CharConsumer action) {
    requireNonNull(action);
    while (this.hasNext()) action.accept(this.nextChar());
  }

  @Override
  default void forEachRemaining(Consumer<? super Character> action) {
    if (action instanceof CharConsumer) {
      this.forEachRemaining((CharConsumer) action);
      return;
    }
    requireNonNull(action);
    this.forEachRemaining((CharConsumer) action::accept);
  }
}
