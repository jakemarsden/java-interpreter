package com.jakemarsden.java.lexer;

import com.jakemarsden.java.lexer.token.Token;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** An {@link Iterator} specialized for {@link Token} values. */
public interface TokenIterator extends Iterator<Token> {

  /**
   * Returns {@code true} if there are more tokens, i.e. if it's safe to call {@link #next()}.
   *
   * @return {@code true} if there are more tokens
   */
  @Override
  boolean hasNext();

  /**
   * Returns the next token.
   *
   * @return the next token
   * @throws NoSuchElementException if there are no more tokens
   */
  @Override
  Token next();

  /**
   * Always throws an {@link UnsupportedOperationException}.
   *
   * @throws UnsupportedOperationException always
   */
  @Override
  default void remove() {
    throw new UnsupportedOperationException();
  }
}
