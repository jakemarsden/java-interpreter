package com.jakemarsden.java.lexer.text;

import static java.util.Objects.requireNonNull;

import java.util.NoSuchElementException;

/** Iterates over the characters in a given character sequence */
public final class StringCharIterator implements CharIterator {

  private final CharSequence value;
  private int position;

  public StringCharIterator(CharSequence value) {
    this.value = requireNonNull(value);
    this.position = 0;
  }

  @Override
  public boolean hasNext() {
    return this.position < this.value.length();
  }

  @Override
  public char nextChar() {
    if (this.position >= this.value.length()) throw new NoSuchElementException();
    return this.value.charAt(this.position++);
  }
}
