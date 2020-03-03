package com.jakemarsden.java.lexer.text;

import java.util.function.Consumer;

/** A {@link Consumer} specialized for {@code char} values. */
@FunctionalInterface
public interface CharConsumer {

  /**
   * Performs this operation on the given argument.
   *
   * @param value the input argument
   */
  void accept(char value);
}
