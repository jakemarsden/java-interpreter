package com.jakemarsden.java.lexer;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface TokenIterator extends Iterator<Token> {

  /**
   * @return {@code true} if there are {@code >=1} tokens remaining, and it's safe to call {@link
   *     #next()}
   */
  @Override
  boolean hasNext();

  /**
   * @return the next token
   * @throws NoSuchElementException if there are no further tokens
   */
  @Override
  Token next() throws NoSuchElementException;

  /** @throws UnsupportedOperationException always */
  @Override
  default void remove() {
    throw new UnsupportedOperationException();
  }
}
