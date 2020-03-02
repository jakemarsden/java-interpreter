package com.jakemarsden.java.lexer.text;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.IntPredicate;

public final class TextParser {

  private final CharIterator text;
  private TextPosition position = TextPosition.start();
  private Queue<Character> buffer = new ArrayDeque<>();

  public TextParser(CharIterator text) {
    this.text = requireNonNull(text);
  }

  public TextPosition getPosition() {
    return this.position;
  }

  public boolean hasNext() {
    return !this.buffer.isEmpty() || this.text.hasNext();
  }

  public char peek() throws NoSuchElementException {
    this.populateBuffer(1);
    return this.buffer.element();
  }

  public char consume() throws NoSuchElementException {
    var c = !this.buffer.isEmpty() ? this.buffer.remove() : this.text.next();
    this.advancePosition(c);
    return c;
  }

  public void consumeExact(char expected) throws IllegalStateException, NoSuchElementException {
    var actual = this.peek();
    if (actual != expected)
      throw new IllegalStateException(format("Expected: '%c' but was: '%c'", expected, actual));
    this.consume();
  }

  public String consumeWhile(IntPredicate predicate) {
    requireNonNull(predicate);
    var fragment = new StringBuilder();
    while (this.hasNext()) {
      var c = this.peek();
      if (!predicate.test(c)) break;
      fragment.append(c);
      this.consume();
    }
    return fragment.toString();
  }

  private void advancePosition(char c) {
    this.position = (c == '\n') ? this.position.nextLine() : this.position.nextColumn();
  }

  private void populateBuffer(int count) throws NoSuchElementException {
    while (this.buffer.size() < count) this.buffer.add(this.text.next());
  }
}
