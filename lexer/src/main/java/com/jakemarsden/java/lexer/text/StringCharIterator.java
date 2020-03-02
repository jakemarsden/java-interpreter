package com.jakemarsden.java.lexer.text;

import static java.util.Objects.requireNonNull;

import java.util.NoSuchElementException;

public final class StringCharIterator implements CharIterator {

  private final String value;
  private int position;
  private int length;

  public StringCharIterator(String value) {
    this.value = requireNonNull(value);
    this.position = 0;
    this.length = value.length();
  }

  @Override
  public boolean hasNext() {
    return this.position < this.length;
  }

  @Override
  public char next() {
    if (this.position >= this.length) throw new NoSuchElementException();
    return this.value.charAt(this.position++);
  }
}
