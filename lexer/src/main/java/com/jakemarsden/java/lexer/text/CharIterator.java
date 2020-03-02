package com.jakemarsden.java.lexer.text;

import java.util.NoSuchElementException;

public interface CharIterator {

  /**
   * @return {@code true} if there are {@code >=1} characters remaining, and it's safe to call
   *     {@link #next()}
   */
  boolean hasNext();

  /**
   * @return the next character
   * @throws NoSuchElementException if there are no further characters
   */
  char next() throws NoSuchElementException;
}
